package org.mi.adminui.security.filter;

import org.mi.adminui.security.service.CustomUserDetailsService;
import org.mi.adminui.security.util.AuthenticationFacade;
import org.mi.adminui.security.util.TokenProvider;
import org.mi.adminui.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mi.adminui.web.core.configuration.constant.AppSecurity.USER_JWT_COOKIE;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationFacade authenticationFacade;
    private final CookieUtils cookieUtils;

    public TokenAuthenticationFilter(TokenProvider tokenProvider,
                                     CustomUserDetailsService customUserDetailsService,
                                     AuthenticationFacade authenticationFacade,
                                     CookieUtils cookieUtils) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationFacade = authenticationFacade;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            cookieUtils.getCookie(request, USER_JWT_COOKIE)
                       .map(cookie -> cookieUtils.deserialize(cookie, String.class))
                       .map(tokenProvider::getUserEmailFromToken)
                       .map(customUserDetailsService::loadUserByUsername)
                       .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))
                       .ifPresent(authentication -> {
                           authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                           authenticationFacade.setAuthentication(authentication);
                       });
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }
        filterChain.doFilter(request, response);
    }
}
