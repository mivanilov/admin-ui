package org.mi.adminui.security.oauth2;

import org.mi.adminui.security.property.AppAuthProperties;
import org.mi.adminui.security.util.CookieUtils;
import org.mi.adminui.security.util.CryptoUtils;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final AppAuthProperties appAuthProperties;
    private final CookieUtils cookieUtils;
    private final CryptoUtils cryptoUtils;

    public CustomAuthenticationSuccessHandler(CustomAuthorizationRequestRepository customAuthorizationRequestRepository,
                                              AppAuthProperties authProperties,
                                              CookieUtils cookieUtils,
                                              CryptoUtils cryptoUtils) {
        this.customAuthorizationRequestRepository = customAuthorizationRequestRepository;
        this.appAuthProperties = authProperties;
        this.cookieUtils = cookieUtils;
        this.cryptoUtils = cryptoUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            logger.warn("Response has already been committed. Unable to redirect to {}", "/");
            return;
        }

        clearAuthenticationAttributes(request);
        customAuthorizationRequestRepository.removeAuthorizationRequestCookie(request, response);

        String encryptedEmail = cryptoUtils.encrypt(((OidcUser) authentication.getPrincipal()).getEmail(), appAuthProperties.getSession().getSecret());
        cookieUtils.addCookie(response, appAuthProperties.getSession().getCookieName(), encryptedEmail, appAuthProperties.getSession().getCookieExpireSec());

        getRedirectStrategy().sendRedirect(request, response, AppRoutes.HOME);
    }
}
