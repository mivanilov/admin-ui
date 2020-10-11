package org.mi.adminui.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailureHandlerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    private CustomAuthenticationFailureHandler failureHandler;

    @BeforeEach
    void setUp() {
        failureHandler = new CustomAuthenticationFailureHandler(customAuthorizationRequestRepository);
    }

    @Test
    void handleAuthenticationFailure() throws IOException {
        failureHandler.onAuthenticationFailure(request, response, null);

        verify(customAuthorizationRequestRepository).removeAuthorizationRequestCookie(request, response);
    }
}
