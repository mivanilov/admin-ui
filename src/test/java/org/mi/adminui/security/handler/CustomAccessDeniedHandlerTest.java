package org.mi.adminui.security.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.security.authentication.AuthenticationFacade;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    private static final String CONTEXT_PATH = "contextPath";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationFacade authenticationFacade;

    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @BeforeEach
    void setUp() {
        customAccessDeniedHandler = new CustomAccessDeniedHandler(authenticationFacade);
    }

    @Test
    void handleUserAccessDeny() throws IOException {
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);

        customAccessDeniedHandler.handle(request, response, new AccessDeniedException(""));

        verify(response).sendRedirect(CONTEXT_PATH + AppRoutes.ACCESS_DENIED);
    }
}
