package org.mi.adminui.security.oauth2;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final CustomOidcUserValidator customOidcUserValidator;

    public CustomOidcUserService(CustomOidcUserValidator customOidcUserValidator) {
        this.customOidcUserValidator = customOidcUserValidator;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        customOidcUserValidator.validateOidcUser(oidcUser);
        return oidcUser;
    }
}
