package com.ryanmhub.fitnessapp.config.jwt;

import com.ryanmhub.fitnessapp.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ryanmhub.fitnessapp.token.TokenType.BEARER;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    //Todo: USe constructor to initialize all autowired
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenRepository tokenRepository;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException { //Todo; How should I handle exceptions. Should I log exceptions and error messages.

        if(request.getServletPath().contains("/api/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;

        if(authHeader == null || !authHeader.startsWith(BEARER.getPrefix())) {
            System.out.println("authHeader null or Does not start with 'Bearer'");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if(!isTokenValid){
            System.out.println("token is not valid");
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails user = jwtService.loadUserWithToken(jwt);
        System.out.println(user);


        //Todo: is the SecurityContext being recreated every request? Make sure when logging out or token is no longer valid to clear securityContext
        if(user != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

}
