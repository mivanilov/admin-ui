package org.mi.adminui.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.util.CookieUtils;
import org.mi.adminui.web.core.configuration.property.AppAuthProperties;
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
import static org.mi.adminui.web.core.configuration.constant.AppSecurity.OAUTH2_AUTH_REQUEST_COOKIE;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2HttpCookieAuthorizationRequestRepositoryTest {

    private static final String SERIALIZED_COOKIE = "SERIALIZED_COOKIE";
    private static final int OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS = 10;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Cookie cookie;

    @Mock
    private CookieUtils cookieUtils;

    private OAuth2HttpCookieAuthorizationRequestRepository cookieRepository;

    @BeforeEach
    void setUp() {
        AppAuthProperties appAuthProperties = new AppAuthProperties(new AppAuthProperties.Token("", 0),
                                                                    new AppAuthProperties.Cookie(0),
                                                                    new AppAuthProperties.Oauth2(OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS));
        cookieRepository = new OAuth2HttpCookieAuthorizationRequestRepository(appAuthProperties, cookieUtils);
    }

    @Test
    void loadAuthorizationRequestFromCookie() {
        OAuth2AuthorizationRequest authorizationRequest = getOAuth2AuthorizationRequest();

        when(cookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE)).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).thenReturn(authorizationRequest);

        assertEquals(authorizationRequest, cookieRepository.loadAuthorizationRequest(request));
    }

    @Test
    void returnNullWhenAuthorizationRequestCookieMissing() {
        when(cookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE)).thenReturn(Optional.empty());

        assertNull(cookieRepository.loadAuthorizationRequest(request));
    }

    @Test
    void saveAuthorizationRequestToCookie() {
        OAuth2AuthorizationRequest authorizationRequest = getOAuth2AuthorizationRequest();

        when(cookieUtils.serialize(authorizationRequest)).thenReturn(SERIALIZED_COOKIE);

        cookieRepository.saveAuthorizationRequest(authorizationRequest, request, response);

        verify(cookieUtils).addCookie(response, OAUTH2_AUTH_REQUEST_COOKIE, SERIALIZED_COOKIE, OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS);
    }

    @Test
    void removeAuthorizationRequestCookieWhenAuthorizationRequestIsNull() {
        cookieRepository.saveAuthorizationRequest(null, request, response);

        assertAll(
                () -> verify(cookieUtils, never()).addCookie(response, OAUTH2_AUTH_REQUEST_COOKIE, SERIALIZED_COOKIE, OAUTH2_AUTH_REQUEST_COOKIE_EXPIRE_SECONDS),
                () -> verify(cookieUtils).deleteCookie(request, response, OAUTH2_AUTH_REQUEST_COOKIE)
        );
    }

    @Test
    void loadAuthorizationRequestFromCookieOnRemoveAuthorizationRequest() {
        OAuth2AuthorizationRequest authorizationRequest = getOAuth2AuthorizationRequest();

        when(cookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE)).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).thenReturn(authorizationRequest);

        OAuth2AuthorizationRequest removedAuthorizationRequest = cookieRepository.removeAuthorizationRequest(request);

        assertAll(
                () -> assertSame(authorizationRequest.getAuthorizationUri(), removedAuthorizationRequest.getAuthorizationUri()),
                () -> assertSame(authorizationRequest.getClientId(), removedAuthorizationRequest.getClientId()),
                () -> assertSame(authorizationRequest.getRedirectUri(), removedAuthorizationRequest.getRedirectUri())
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
