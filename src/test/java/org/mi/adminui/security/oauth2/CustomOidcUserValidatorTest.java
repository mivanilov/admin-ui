package org.mi.adminui.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomOidcUserValidatorTest {

    private static final String EMAIL = "email";

    @Mock
    private OidcUser oidcUser;

    @Mock
    private UserService userService;

    private CustomOidcUserValidator customOidcUserValidator;

    @BeforeEach
    void setUp() {
        customOidcUserValidator = new CustomOidcUserValidator(userService);
    }

    @Test
    void oidcUserValid() {
        when(oidcUser.getEmail()).thenReturn(EMAIL);
        when(userService.find(EMAIL)).thenReturn(Optional.of(new User()));

        customOidcUserValidator.validateOidcUser(oidcUser);
    }

    @Test
    void oidcUserInvalid() {
        when(oidcUser.getEmail()).thenReturn(EMAIL);
        when(userService.find(EMAIL)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customOidcUserValidator.validateOidcUser(oidcUser));
    }
}
