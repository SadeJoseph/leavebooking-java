package com.example.leavebooking.identity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http)
      throws Exception {

    return http
        // JWT authentication is stateless, so disable CSRF here.
        .csrf(AbstractHttpConfigurer::disable)

        .authorizeHttpRequests(auth -> auth
            // Registration/login endpoints must be accessible before a user has authenticated.
            .requestMatchers("/auth/**").permitAll()

            // Everything else requires authentication.
            .anyRequest().authenticated())

        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                .jwtAuthenticationConverter(
                    jwtAuthenticationConverter)))

        .build();
  }
}