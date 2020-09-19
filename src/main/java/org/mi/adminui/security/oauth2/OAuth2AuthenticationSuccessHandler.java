package org.mi.adminui.security.oauth2;

import org.mi.adminui.security.util.TokenProvider;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mi.adminui.web.core.configuration.constant.AppSecurity.TOKEN;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final TokenProvider tokenProvider;
    private final OAuth2HttpCookieAuthorizationRequestRepository cookieRepository;

    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider,
                                              OAuth2HttpCookieAuthorizationRequestRepository cookieRepository) {
        this.tokenProvider = tokenProvider;
        this.cookieRepository = cookieRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to {}", AppRoutes.ACCESS_LOGIN_SUCCESS);
            return;
        }

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, resolveTargetUrl(authentication));
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieRepository.removeAuthorizationRequestCookie(request, response);
    }

    private String resolveTargetUrl(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        return UriComponentsBuilder.fromUriString(AppRoutes.ACCESS_LOGIN_SUCCESS)
                                   .queryParam(TOKEN, tokenProvider.createToken(oidcUser.getEmail()))
                                   .build()
                                   .toUriString();
    }
}
