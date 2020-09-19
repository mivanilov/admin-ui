package org.mi.adminui.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.security.util.TokenProvider;
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
class OAuth2AuthenticationSuccessHandlerTest {

    private static final String EMAIL = "EMAIL";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Mock
    private OidcUser oidcUser;

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private OAuth2HttpCookieAuthorizationRequestRepository cookieRepository;

    private OAuth2AuthenticationSuccessHandler successHandler;

    @BeforeEach
    void setUp() {
        successHandler = new OAuth2AuthenticationSuccessHandler(tokenProvider, cookieRepository);
    }

    @Test
    void handleAuthenticationSuccess() throws IOException {
        when(response.isCommitted()).thenReturn(false);
        when(authentication.getPrincipal()).thenReturn(oidcUser);
        when(oidcUser.getEmail()).thenReturn(EMAIL);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertAll(
                () -> verify(cookieRepository).removeAuthorizationRequestCookie(request, response),
                () -> verify(tokenProvider).createToken(EMAIL)
        );
    }

    @Test
    void handleCommittedResponse() throws IOException {
        when(response.isCommitted()).thenReturn(true);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertAll(
                () -> verifyNoInteractions(cookieRepository),
                () -> verifyNoInteractions(tokenProvider)
        );
    }
}
