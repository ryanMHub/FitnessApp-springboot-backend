package com.ryanmhub.fitnessapp.config;

import com.ryanmhub.fitnessapp.config.jwt.JwtAuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh-token"
    };

    private final JwtAuthTokenFilter jwtAuthTokenFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;

    public SecurityConfig(JwtAuthTokenFilter jwtAuthTokenFilter, AuthenticationProvider authenticationProvider, LogoutService logoutService) {
        this.jwtAuthTokenFilter = jwtAuthTokenFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutService = logoutService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest().authenticated()
                        //Todo: .requireSecure() add HTTPS
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/auth/logout")
                                .addLogoutHandler(logoutService)
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_OK);
                                    response.getWriter().write("{\"message\":\"Logout Successful\",\"success\":true, \"logout_info\":\"It worked\"}");
                                })
                );

        return http.build();
    }
}
