package org.mi.adminui.security.oauth2;

import org.apache.commons.lang3.StringUtils;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mi.adminui.security.exception.OAuth2AuthenticationProcessingException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class OAuth2OidcUserValidator {

    private final UserService userService;

    public OAuth2OidcUserValidator(UserService userService) {
        this.userService = userService;
    }

    public void validateOidcUser(OidcUser oidcUser) {
        if (StringUtils.isBlank(oidcUser.getEmail()) || userService.find(oidcUser.getEmail()).isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("User not found by email: " + oidcUser.getEmail());
        }
    }
}
