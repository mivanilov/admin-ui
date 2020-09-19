package org.mi.adminui.security.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.security.service.CustomUserDetailsService;
import org.mi.adminui.security.util.AuthenticationFacade;
import org.mi.adminui.security.util.TokenProvider;
import org.mi.adminui.util.CookieUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mi.adminui.web.core.configuration.constant.AppSecurity.USER_JWT_COOKIE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {

    private static final String TOKEN = "TOKEN";
    private static final String EMAIL = "EMAIL";

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
    private TokenProvider tokenProvider;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private CookieUtils cookieUtils;

    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @BeforeEach
    void setUp() {
        tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenProvider, customUserDetailsService, authenticationFacade, cookieUtils);
    }

    @Test
    void setAuthentication() throws ServletException, IOException {
        when(cookieUtils.getCookie(request, USER_JWT_COOKIE)).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, String.class)).thenReturn(TOKEN);
        when(tokenProvider.getUserEmailFromToken(TOKEN)).thenReturn(EMAIL);
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertAll(
                () -> verify(authenticationFacade).setAuthentication(any(Authentication.class)),
                () -> verify(filterChain).doFilter(request, response)
        );
    }

    @Test
    void doNotSetAuthenticationWhenNoJwtFound() throws ServletException, IOException {
        when(cookieUtils.getCookie(request, USER_JWT_COOKIE)).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, String.class)).thenReturn("");

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertAll(
                () -> verifyNoMoreInteractions(authenticationFacade),
                () -> verify(filterChain).doFilter(request, response)
        );
    }

    @Test
    void handleException() throws ServletException, IOException {
        when(cookieUtils.getCookie(request, USER_JWT_COOKIE)).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, String.class)).thenReturn(TOKEN);
        when(tokenProvider.getUserEmailFromToken(TOKEN)).thenThrow(new RuntimeException());

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertAll(
                () -> verifyNoMoreInteractions(authenticationFacade),
                () -> verify(filterChain).doFilter(request, response)
        );
    }
}
