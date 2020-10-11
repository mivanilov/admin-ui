package org.mi.adminui.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.security.property.AppAuthProperties;
import org.mi.adminui.security.util.CookieUtils;
import org.mi.adminui.security.util.CryptoUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

    private static final String EMAIL = "email";
    private static final String ENCRYPTED_EMAIL = "encryptedEmail";
    private static final String SESSION_COOKIE_NAME = "name";
    private static final String SESSION_COOKIE_SECRET = "secret";
    private static final int SESSION_COOKIE_EXPIRE_SEC = 60;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Mock
    private OidcUser oidcUser;

    @Mock
    private CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    @Mock
    private CookieUtils cookieUtils;
    @Mock
    private CryptoUtils cryptoUtils;

    private CustomAuthenticationSuccessHandler successHandler;

    @BeforeEach
    void setUp() {
        AppAuthProperties appAuthProperties = new AppAuthProperties(new AppAuthProperties.Session(SESSION_COOKIE_SECRET, SESSION_COOKIE_NAME, SESSION_COOKIE_EXPIRE_SEC), null);
        successHandler = new CustomAuthenticationSuccessHandler(customAuthorizationRequestRepository, appAuthProperties, cookieUtils, cryptoUtils);
    }

    @Test
    void handleAuthenticationSuccess() throws IOException {
        when(response.isCommitted()).thenReturn(false);
        when(authentication.getPrincipal()).thenReturn(oidcUser);
        when(oidcUser.getEmail()).thenReturn(EMAIL);
        when(cryptoUtils.encrypt(EMAIL, SESSION_COOKIE_SECRET)).thenReturn(ENCRYPTED_EMAIL);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertAll(
                () -> verify(customAuthorizationRequestRepository).removeAuthorizationRequestCookie(request, response),
                () -> verify(cookieUtils).addCookie(response, SESSION_COOKIE_NAME, ENCRYPTED_EMAIL, SESSION_COOKIE_EXPIRE_SEC)
        );
    }

    @Test
    void handleCommittedResponse() throws IOException {
        when(response.isCommitted()).thenReturn(true);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertAll(
                () -> verifyNoInteractions(customAuthorizationRequestRepository),
                () -> verifyNoInteractions(cookieUtils),
                () -> verifyNoInteractions(cryptoUtils)
        );
    }
}
