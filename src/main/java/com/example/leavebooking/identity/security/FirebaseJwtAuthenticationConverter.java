package com.example.leavebooking.identity.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class FirebaseJwtAuthenticationConverter
    implements Converter<Jwt, AbstractAuthenticationToken> {

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {

    String roleClaim = Objects.requireNonNull(
        jwt.getClaimAsString("role"));

    Collection<GrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(roleClaim));

    return new JwtAuthenticationToken(
        jwt,
        authorities,
        Objects.requireNonNull(jwt.getSubject()));
  }
}