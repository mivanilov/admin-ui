package org.mi.adminui.security.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.security.util.AuthenticationFacade;
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
class UserAccessDeniedHandlerTest {

    private static final String CONTEXT_PATH = "CONTEXT_PATH";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationFacade authenticationFacade;

    private UserAccessDeniedHandler userAccessDeniedHandler;

    @BeforeEach
    void setUp() {
        userAccessDeniedHandler = new UserAccessDeniedHandler(authenticationFacade);
    }

    @Test
    void handleUserAccessDeny() throws IOException {
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);

        userAccessDeniedHandler.handle(request, response, new AccessDeniedException(""));

        verify(response).sendRedirect(CONTEXT_PATH + AppRoutes.ACCESS_DENIED);
    }
}
