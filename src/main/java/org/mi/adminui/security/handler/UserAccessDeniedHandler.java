package org.mi.adminui.security.handler;

import org.mi.adminui.security.util.AuthenticationFacade;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserAccessDeniedHandler.class);

    private final AuthenticationFacade authenticationFacade;

    public UserAccessDeniedHandler(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        Authentication auth = authenticationFacade.getAuthentication();

        if (auth != null) {
            logger.info("{} tried to access protected resource: {}", auth.getName(), request.getRequestURI());
        }

        response.sendRedirect(request.getContextPath() + AppRoutes.ACCESS_DENIED);
    }
}