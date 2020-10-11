package org.mi.adminui.security.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.security.authentication.AuthenticationFacade;
import org.mi.adminui.security.authentication.CustomUserAuthenticationToken;
import org.mi.adminui.security.property.AppAuthProperties;
import org.mi.adminui.security.userdetails.CustomUserDetailsService;
import org.mi.adminui.security.util.CookieUtils;
import org.mi.adminui.security.util.CryptoUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserAuthenticationFilterTest {

    private static final String ENCRYPTED_EMAIL = "encryptedEmail";
    private static final String EMAIL = "email";
    private static final String SESSION_COOKIE_NAME = "name";
    private static final String SESSION_COOKIE_SECRET = "secret";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Cookie cookie;
    @Mock
    private UserDetails userDetails;

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private CookieUtils cookieUtils;
    @Mock
    private CryptoUtils cryptoUtils;

    private CustomUserAuthenticationFilter customUserAuthenticationFilter;

    @BeforeEach
    void setUp() {
        AppAuthProperties appAuthProperties = new AppAuthProperties(new AppAuthProperties.Session(SESSION_COOKIE_SECRET, SESSION_COOKIE_NAME, 0), null);
        customUserAuthenticationFilter = new CustomUserAuthenticationFilter(customUserDetailsService, authenticationFacade, appAuthProperties, cookieUtils, cryptoUtils);
    }

    @Test
    void setAuthentication() throws ServletException, IOException {
        when(cookieUtils.getCookie(request, SESSION_COOKIE_NAME)).thenReturn(Optional.of(cookie));
        when(cookie.getValue()).thenReturn(ENCRYPTED_EMAIL);
        when(cryptoUtils.decrypt(ENCRYPTED_EMAIL, SESSION_COOKIE_SECRET)).thenReturn(EMAIL);
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);

        customUserAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertAll(
                () -> verify(authenticationFacade).setAuthentication(any(CustomUserAuthenticationToken.class)),
                () -> verify(filterChain).doFilter(request, response)
        );
    }

    @Test
    void doNotSetAuthenticationWhenNoSessionCookieFound() throws ServletException, IOException {
        when(cookieUtils.getCookie(request, SESSION_COOKIE_NAME)).thenReturn(Optional.of(cookie));
        when(cookie.getValue()).thenReturn("");

        customUserAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertAll(
                () -> verifyNoMoreInteractions(authenticationFacade),
                () -> verify(filterChain).doFilter(request, response)
        );
    }

    @Test
    void handleException() throws ServletException, IOException {
        when(cookieUtils.getCookie(request, SESSION_COOKIE_NAME)).thenThrow(new RuntimeException());

        customUserAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertAll(
                () -> verifyNoMoreInteractions(authenticationFacade),
                () -> verify(filterChain).doFilter(request, response)
        );
    }
}
