package com.ryanmhub.fitnessapp.auth.service;

import com.ryanmhub.fitnessapp.auth.dto.LoginDTO;
import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.role.response.RegistrationResponse;
import com.ryanmhub.fitnessapp.config.jwt.JwtService;
import com.ryanmhub.fitnessapp.auth.response.AuthenticationResponse;
import com.ryanmhub.fitnessapp.role.model.Role;
import com.ryanmhub.fitnessapp.role.model.UserRole;
import com.ryanmhub.fitnessapp.role.repository.RoleRepository;
import com.ryanmhub.fitnessapp.role.repository.UserRoleRepository;
import com.ryanmhub.fitnessapp.token.TokenService;
import com.ryanmhub.fitnessapp.user.dto.AppUserDTO;
import com.ryanmhub.fitnessapp.user.model.AppUser;
import com.ryanmhub.fitnessapp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

//Todo: Does the refresh token system work
//Todo: I believe I should only have repositories being called from that classes service, so for user userRepository should be called in UserService, so all other classes would need to go through through that service
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final UserRoleRepository userRoleRepository;
    private final TokenService tokenService;


    public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService, RoleRepository roleRepository, UserRoleRepository userRoleRepository, TokenService tokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    //Uses the data sent by client DTO to validate through authentication manager, then creates a JWT tokens for the user
    public ResponseEntity<ApiResponse> authenticateUser(LoginDTO loginDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsernameOrEmail(),
                        loginDTO.getPassword()
                )
        );


        var user = userService.findByUsernameOrEmail(loginDTO.getUsernameOrEmail()).orElseThrow();

        var jwtToken = jwtService.generateJwtToken(Map.of("roles", jwtService.serializeObject(user.getRolesNames())), user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, jwtToken);

        return new ResponseEntity<>(AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).message("Authorized").success(true).build(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ApiResponse> registerUser(AppUserDTO userDTO){

        //The first two if statements determine if the username or email has already been used. If it has return IM_USED response to client
        if(userService.existsByUsername(userDTO.getUsername())) {
            return new ResponseEntity<>(RegistrationResponse.builder().success(false).message("Username is already taken!").build(), HttpStatus.IM_USED);
        }


        if(userService.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>(RegistrationResponse.builder().success(false).message("Email Address already in use!").build(), HttpStatus.IM_USED);
        }

        //after determining that the username and email are not already in use build the user entity
        AppUser user = new AppUser.Builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword())) //encode the password before adding it to the user entity
                .build();

        var savedUser = userService.saveUser(user); //Save the newly created user to the DB
        Role roleName =  roleRepository.findById(2L).orElse(new Role.Builder().name("GUEST").build());
        var userRole = new UserRole.Builder().role(roleName).user(savedUser).build(); //Create a new UserRole using the newly created user entity. And the Role give by the new user.
        userRoleRepository.save(userRole); //save new UserRole that links the user to the type of role to the UserRole table in the database

        return new ResponseEntity<>(RegistrationResponse.builder().success(true).message("User registered successfully").build(), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if(authHeader == null || !authHeader.startsWith(BEARER.getPrefix())){
            return new ResponseEntity<>(AuthenticationResponse.builder().success(false).message("Invalid Header").build(), HttpStatus.UNAUTHORIZED);
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if(username == null) return new ResponseEntity<>(AuthenticationResponse.builder().success(false).message("Invalid Refresh Token").build(), HttpStatus.UNAUTHORIZED);

        var user = this.userService.findByUsernameOrEmail(username).orElseThrow();
        if(!jwtService.isTokenValid(refreshToken, user)) return new ResponseEntity<>(AuthenticationResponse.builder().success(false).message("Invalid Refresh Token").build(), HttpStatus.UNAUTHORIZED);

        var accessToken = jwtService.generateJwtToken(user);
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, accessToken);
        tokenService.saveUserToken(user, refreshToken);

        return new ResponseEntity<>(AuthenticationResponse.builder().success(true).message("Access Token Refreshed").build(), HttpStatus.OK);
    }


}
