package org.mi.adminui.security.filter;

import org.mi.adminui.security.authentication.AuthenticationFacade;
import org.mi.adminui.security.authentication.CustomUserAuthenticationToken;
import org.mi.adminui.security.property.AppAuthProperties;
import org.mi.adminui.security.userdetails.CustomUserDetailsService;
import org.mi.adminui.security.util.CookieUtils;
import org.mi.adminui.security.util.CryptoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomUserAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserAuthenticationFilter.class);

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationFacade authenticationFacade;
    private final AppAuthProperties appAuthProperties;
    private final CookieUtils cookieUtils;
    private final CryptoUtils cryptoUtils;

    public CustomUserAuthenticationFilter(CustomUserDetailsService customUserDetailsService,
                                          AuthenticationFacade authenticationFacade,
                                          AppAuthProperties appAuthPropertie,
                                          CookieUtils cookieUtils,
                                          CryptoUtils cryptoUtils) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationFacade = authenticationFacade;
        this.appAuthProperties = appAuthPropertie;
        this.cookieUtils = cookieUtils;
        this.cryptoUtils = cryptoUtils;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            cookieUtils.getCookie(request, appAuthProperties.getSession().getCookieName())
                       .map(Cookie::getValue)
                       .map(encryptedEmail -> cryptoUtils.decrypt(encryptedEmail, appAuthProperties.getSession().getSecret()))
                       .map(customUserDetailsService::loadUserByUsername)
                       .map(CustomUserAuthenticationToken::new)
                       .ifPresent(authenticationFacade::setAuthentication);
        } catch (Exception e) {
            logger.error("Failed to set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }
}
