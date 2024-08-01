package com.ryanmhub.fitnessapp.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanmhub.fitnessapp.auth.dto.LoginDTO;
import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.role.response.RegistrationResponse;
import com.ryanmhub.fitnessapp.config.jwt.JwtService;
import com.ryanmhub.fitnessapp.auth.response.AuthenticationResponse;
import com.ryanmhub.fitnessapp.role.model.Role;
import com.ryanmhub.fitnessapp.role.model.UserRole;
import com.ryanmhub.fitnessapp.role.repository.RoleRepository;
import com.ryanmhub.fitnessapp.role.repository.UserRoleRepository;
import com.ryanmhub.fitnessapp.token.Token;
import com.ryanmhub.fitnessapp.token.TokenRepository;
import com.ryanmhub.fitnessapp.user.dto.AppUserDTO;
import com.ryanmhub.fitnessapp.user.model.AppUser;
import com.ryanmhub.fitnessapp.user.repository.AppUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static com.ryanmhub.fitnessapp.token.TokenType.BEARER;

//Todo: *******************How do we safe guard the app so only logged in users can access functionality of the site. And how does the application know if a user is logged in or not. Through IP, Mac address, Chached data******************************
@Service
public class AuthService {

    //Todo: Constructor for injection
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EntityManager entityManager;

    //Todo: ON exception return response that username or password were wrong. Don't be specific
    public ResponseEntity<ApiResponse> authenticateUser(LoginDTO loginDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsernameOrEmail(),
                        loginDTO.getPassword()
                )
        );

        var user = userRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail()).orElseThrow();

        var jwtToken = jwtService.generateJwtToken(Map.of("roles", jwtService.serializeObject(user.getRolesNames())), user); //Todo: Fix Roles it is currently empty
        var refreshToken = jwtService.generateRefreshToken(user); //Todo: How are we storing and checking and utilizing refreshToken
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken); //Todo: this should probably be switched with the refresh token. Meaning we should save the refresh token to the server. Not the access Token.

        return new ResponseEntity<>(AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).message("Authorized").success(true).build(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ApiResponse> registerUser(AppUserDTO userDTO){

        //The first two if statements determine if the username or email has already been used. If it has return IM_USED response to client
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            return new ResponseEntity<>(RegistrationResponse.builder().success(false).message("Username is already taken!").build(), HttpStatus.IM_USED); //Todo: Should this be passed to the client??? And how would I do that
        }

        //Todo: Check that password is strong enough. Maybe this should be handled only on the client side.
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>(RegistrationResponse.builder().success(false).message("Email Address already in use!").build(), HttpStatus.IM_USED); //Todo: Same as above. As well as any other data that is needed from the user at registration
        }

        //after determining that the username and email are not already in use build the user entity
        AppUser user = new AppUser.Builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword())) //encode the password before adding it to the user entity
                .build();

        var savedUser = userRepository.save(user); //Save the newly created user to the DB
        Role roleName =  roleRepository.findById(2L).orElse(new Role.Builder().name("GUEST").build()); //Todo: Is there a better approach to handling
        var userRole = new UserRole.Builder().role(roleName).user(savedUser).build(); //Create a new UserRole using the newly created user entity. And the Role give by the new user. Todo: get role from client
        userRoleRepository.save(userRole); //save new UserRole that links the user to the type of role to the UserRole table in the database

        return new ResponseEntity<>(RegistrationResponse.builder().success(true).message("User registered successfully").build(), HttpStatus.OK); //Todo: This needs to be passed to the Client once the user is registered
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if(authHeader == null || !authHeader.startsWith(BEARER.getPrefix())){
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if(username != null) {
            //Todo: Should we get the user based on the current user accessing the endpoint. Not based on the username in the token. Then compare the token to the current user.
            var user = this.userRepository.findByUsernameOrEmail(username, username).orElseThrow(); //Todo: Handle this exception
            if(jwtService.isTokenValid(refreshToken, user)){
                var accessToken = jwtService.generateJwtToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = new AuthenticationResponse.Builder().accessToken(accessToken).refreshToken(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    //private Service Utility functions
    //Todo: should I delete the tokens. How do the Tokens get cleaned up and not litter the DB
    //Todo: should this be in the token service class. That doesn't exist yet
    private void revokeAllUserTokens(AppUser user) {
        var validTokens = tokenRepository.findAllValidTokenByAppUser(user.getId());
        if (validTokens.isEmpty()) return;
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(AppUser user, String jwtToken){
        var token = new Token.Builder()
                .user(user)
                .token(jwtToken)
                .tokenType(BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


}
