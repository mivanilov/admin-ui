package org.mi.adminui.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mi.adminui.security.exception.OAuth2AuthenticationProcessingException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2OidcUserValidatorTest {

    private static final String EMAIL = "EMAIL";

    @Mock
    private OidcUser oidcUser;

    @Mock
    private UserService userService;

    private OAuth2OidcUserValidator oAuth2OidcUserValidator;

    @BeforeEach
    void setUp() {
        oAuth2OidcUserValidator = new OAuth2OidcUserValidator(userService);
    }

    @Test
    void oidcUserValid() {
        when(oidcUser.getEmail()).thenReturn(EMAIL);
        when(userService.find(EMAIL)).thenReturn(Optional.of(new User()));

        oAuth2OidcUserValidator.validateOidcUser(oidcUser);
    }

    @Test
    void oidcUserInvalid() {
        when(oidcUser.getEmail()).thenReturn(EMAIL);
        when(userService.find(EMAIL)).thenReturn(Optional.empty());

        assertThrows(OAuth2AuthenticationProcessingException.class, () -> oAuth2OidcUserValidator.validateOidcUser(oidcUser));
    }
}
