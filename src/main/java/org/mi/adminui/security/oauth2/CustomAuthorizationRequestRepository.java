package org.mi.adminui.security.oauth2;

import org.mi.adminui.security.util.CookieUtils;
import org.mi.adminui.security.property.AppAuthProperties;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final AppAuthProperties appAuthProperties;
    private final CookieUtils cookieUtils;

    public CustomAuthorizationRequestRepository(AppAuthProperties appAuthProperties,
                                                CookieUtils cookieUtils) {
        this.appAuthProperties = appAuthProperties;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest != null) {
            cookieUtils.addCookie(response, appAuthProperties.getOauth2().getAuthRequestCookieName(), cookieUtils.serialize(authorizationRequest),
                                  appAuthProperties.getOauth2().getAuthRequestCookieExpireSec());
        } else {
            removeAuthorizationRequestCookie(request, response);
        }
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return getAuthorizationRequest(request);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest auth2AuthorizationRequest = getAuthorizationRequest(request);
        removeAuthorizationRequestCookie(request, response);
        return auth2AuthorizationRequest;
    }

    @Override
    @Deprecated
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return getAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookie(HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.deleteCookie(request, response, appAuthProperties.getOauth2().getAuthRequestCookieName());
    }

    private OAuth2AuthorizationRequest getAuthorizationRequest(HttpServletRequest request) {
        return cookieUtils.getCookie(request, appAuthProperties.getOauth2().getAuthRequestCookieName())
                          .map(cookie -> cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                          .orElse(null);
    }
}
