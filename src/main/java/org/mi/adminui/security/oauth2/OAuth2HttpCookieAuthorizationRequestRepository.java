package org.mi.adminui.security.oauth2;

import org.mi.adminui.util.CookieUtils;
import org.mi.adminui.web.core.configuration.property.AppAuthProperties;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mi.adminui.web.core.configuration.constant.AppSecurity.OAUTH2_AUTH_REQUEST_COOKIE;

@Component
public class OAuth2HttpCookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final AppAuthProperties appAuthProperties;
    private final CookieUtils cookieUtils;

    public OAuth2HttpCookieAuthorizationRequestRepository(AppAuthProperties appAuthProperties,
                                                          CookieUtils cookieUtils) {
        this.appAuthProperties = appAuthProperties;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return cookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE)
                          .map(cookie -> cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                          .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest != null) {
            cookieUtils.addCookie(response,
                                  OAUTH2_AUTH_REQUEST_COOKIE,
                                  cookieUtils.serialize(authorizationRequest),
                                  appAuthProperties.getOauth2().getAuthRequestCookieExpireSec());
        } else {
            removeAuthorizationRequestCookie(request, response);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookie(HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.deleteCookie(request, response, OAUTH2_AUTH_REQUEST_COOKIE);
    }
}
