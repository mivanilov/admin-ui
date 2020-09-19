package org.mi.adminui.security.oauth2;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OAuth2OidcUserService extends OidcUserService {

    private final OAuth2OidcUserValidator oAuth2OidcUserValidator;

    public OAuth2OidcUserService(OAuth2OidcUserValidator oAuth2OidcUserValidator) {
        this.oAuth2OidcUserValidator = oAuth2OidcUserValidator;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        oAuth2OidcUserValidator.validateOidcUser(oidcUser);

        return oidcUser;
    }
}
