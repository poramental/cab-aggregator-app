package com.modsen.driverservice.util;

import com.modsen.driverservice.dto.DriverRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;
import com.modsen.driverservice.security.User;
public class SecurityUtil {
    public static final String ACTUATOR = "/actuator/**";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ID = "sub";
    public static final String USERNAME = "preferred_username";
    public static final String GIVEN_NAME = "given_name";
    public static final String FAMILY_NAME = "family_name";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String USER_NAME = "username";
    public static final String REALM_ACCESS = "realm_access";
    public static final String ROLES = "roles";

    public static DriverRequest getPassengerRequestFromOauth2User(OAuth2User oAuth2User) {
        return DriverRequest.builder()
                .name(oAuth2User.getAttribute(NAME))
                .surname(oAuth2User.getAttribute(SURNAME))
                .email(oAuth2User.getAttribute(EMAIL))
                .phone(oAuth2User.getAttribute(PHONE))
                .build();
    }

    public static User extractUserInfo(Jwt jwt) {
        return User.builder()
                .phone(jwt.getClaim(PHONE))
                .surname(jwt.getClaim(FAMILY_NAME))
                .name(jwt.getClaim(GIVEN_NAME))
                .id(UUID.fromString(jwt.getClaim(ID)))
                .email(jwt.getClaim(EMAIL))
                .username(jwt.getClaim(USERNAME))
                .build();
    }
}
