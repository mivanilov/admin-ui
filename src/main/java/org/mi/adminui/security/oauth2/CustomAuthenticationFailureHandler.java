package org.mi.adminui.security.oauth2;

import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    public CustomAuthenticationFailureHandler(CustomAuthorizationRequestRepository customAuthorizationRequestRepository) {
        this.customAuthorizationRequestRepository = customAuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        customAuthorizationRequestRepository.removeAuthorizationRequestCookie(request, response);
        getRedirectStrategy().sendRedirect(request, response, AppRoutes.ERROR);
    }
}
