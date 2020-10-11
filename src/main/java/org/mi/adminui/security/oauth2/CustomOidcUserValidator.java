package org.mi.adminui.security.oauth2;

import org.apache.commons.lang3.StringUtils;
import org.mi.adminui.data.feature.user.service.UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class CustomOidcUserValidator {

    private final UserService userService;

    public CustomOidcUserValidator(UserService userService) {
        this.userService = userService;
    }

    public void validateOidcUser(OidcUser oidcUser) {
        if (StringUtils.isBlank(oidcUser.getEmail()) || userService.find(oidcUser.getEmail()).isEmpty()) {
            throw new RuntimeException("User not found by email: " + oidcUser.getEmail());
        }
    }
}
