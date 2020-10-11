package org.mi.adminui.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.security.property.AppAuthProperties;
import org.mi.adminui.security.util.CookieUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthorizationRequestRepositoryTest {

    private static final String OAUTH2_AUTH_REQUEST_COOKIE = "serializedCookie";
    private static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "name";
    private static final int OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS = 60;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Cookie cookie;

    @Mock
    private CookieUtils cookieUtils;

    private CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @BeforeEach
    void setUp() {
        AppAuthProperties appAuthProperties = new AppAuthProperties(null,
                                                                    new AppAuthProperties.Oauth2(OAUTH2_AUTH_REQUEST_COOKIE_NAME, OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS));
        customAuthorizationRequestRepository = new CustomAuthorizationRequestRepository(appAuthProperties, cookieUtils);
    }

    @Test
    void loadAuthorizationRequestFromCookie() {
        OAuth2AuthorizationRequest authorizationRequest = getOAuth2AuthorizationRequest();

        when(cookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME)).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).thenReturn(authorizationRequest);

        assertEquals(authorizationRequest, customAuthorizationRequestRepository.loadAuthorizationRequest(request));
    }

    @Test
    void returnNullWhenAuthorizationRequestCookieMissing() {
        when(cookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME)).thenReturn(Optional.empty());

        assertNull(customAuthorizationRequestRepository.loadAuthorizationRequest(request));
    }

    @Test
    void saveAuthorizationRequestToCookie() {
        OAuth2AuthorizationRequest authorizationRequest = getOAuth2AuthorizationRequest();

        when(cookieUtils.serialize(authorizationRequest)).thenReturn(OAUTH2_AUTH_REQUEST_COOKIE);

        customAuthorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request, response);

        verify(cookieUtils).addCookie(response, OAUTH2_AUTH_REQUEST_COOKIE_NAME, OAUTH2_AUTH_REQUEST_COOKIE, OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS);
    }

    @Test
    void removeAuthorizationRequestCookieWhenAuthorizationRequestIsNull() {
        customAuthorizationRequestRepository.saveAuthorizationRequest(null, request, response);

        assertAll(
                () -> verify(cookieUtils, never()).addCookie(response, OAUTH2_AUTH_REQUEST_COOKIE_NAME, OAUTH2_AUTH_REQUEST_COOKIE, OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS),
                () -> verify(cookieUtils).deleteCookie(request, response, OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        );
    }

    @Test
    void loadAuthorizationRequestFromCookieOnRemoveAuthorizationRequest() {
        OAuth2AuthorizationRequest authorizationRequest = getOAuth2AuthorizationRequest();

        when(cookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME)).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).thenReturn(authorizationRequest);

        OAuth2AuthorizationRequest removedAuthorizationRequest = customAuthorizationRequestRepository.removeAuthorizationRequest(request, response);

        assertAll(
                () -> assertSame(authorizationRequest.getAuthorizationUri(), removedAuthorizationRequest.getAuthorizationUri()),
                () -> assertSame(authorizationRequest.getClientId(), removedAuthorizationRequest.getClientId()),
                () -> assertSame(authorizationRequest.getRedirectUri(), removedAuthorizationRequest.getRedirectUri()),
                () -> verify(cookieUtils).deleteCookie(request, response, OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        );
    }

    private OAuth2AuthorizationRequest getOAuth2AuthorizationRequest() {
        return OAuth2AuthorizationRequest.authorizationCode()
                                         .authorizationUri("https://authorizationUri")
                                         .clientId("clientId")
                                         .redirectUri("https://redirectUri")
                                         .build();
    }
}
