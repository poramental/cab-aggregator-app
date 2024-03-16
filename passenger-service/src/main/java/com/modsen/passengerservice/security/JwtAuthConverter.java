package com.modsen.passengerservice.security;

import org.springframework.core.convert.converter.Converter;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.modsen.passengerservice.util.SecurityUtil.*;

@RequiredArgsConstructor
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final PassengerService passengerService;
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties properties;

    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        User user = passengerService.extractUserInfo(jwt);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return authToken;
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
        Collection<String> resourceRoles;
        System.out.println(realmAccess.get(properties.getResourceId()));
        if (realmAccess == null
                || (resourceRoles = (Collection<String>) realmAccess.get(ROLES)) == null) {
            return Set.of();
        }

        return resourceRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}