package org.mi.adminui.security.configuration;

import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.security.filter.TokenAuthenticationFilter;
import org.mi.adminui.security.handler.UserAccessDeniedHandler;
import org.mi.adminui.security.oauth2.OAuth2AuthenticationFailureHandler;
import org.mi.adminui.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.mi.adminui.security.oauth2.OAuth2HttpCookieAuthorizationRequestRepository;
import org.mi.adminui.security.oauth2.OAuth2OidcUserService;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.mi.adminui.web.core.configuration.constant.AppSecurity.USER_JWT_COOKIE;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final OAuth2OidcUserService oAuth2OidcUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2HttpCookieAuthorizationRequestRepository oAuth2HttpCookieAuthorizationRequestRepository;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final UserAccessDeniedHandler userAccessDeniedHandler;

    public SecurityConfiguration(OAuth2OidcUserService oAuth2OidcUserService,
                                 OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                 OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                                 OAuth2HttpCookieAuthorizationRequestRepository oAuth2HttpCookieAuthorizationRequestRepository,
                                 TokenAuthenticationFilter tokenAuthenticationFilter,
                                 UserAccessDeniedHandler userAccessDeniedHandler) {
        this.oAuth2OidcUserService = oAuth2OidcUserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.oAuth2HttpCookieAuthorizationRequestRepository = oAuth2HttpCookieAuthorizationRequestRepository;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.userAccessDeniedHandler = userAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        sessionManagement(httpSecurity);
        csrf(httpSecurity);
        authorizeRequests(httpSecurity);
        oauth2Login(httpSecurity);
        logout(httpSecurity);
        exceptionHandling(httpSecurity);
        filters(httpSecurity);
    }

    private void sessionManagement(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void csrf(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable();
    }

    private void authorizeRequests(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers(AppRoutes.HOME,
                             AppRoutes.ERROR,
                             AppRoutes.ACCESS_LOGIN_SUCCESS,
                             "/robots.txt",
                             "/favicon.ico",
                             "/actuator/**",
                             "/js/**",
                             "/css/**",
                             "/img/**",
                             "/webjars/**").permitAll()
                .antMatchers(AppRoutes.USERS + "/**").hasRole(User.RoleType.ADMIN.name())
                .anyRequest().authenticated();
    }

    private void oauth2Login(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .oauth2Login()
                .loginPage(AppRoutes.ACCESS_LOGIN).permitAll()
                .authorizationEndpoint()
                    .authorizationRequestRepository(oAuth2HttpCookieAuthorizationRequestRepository)
                    .and()
                .userInfoEndpoint()
                    .oidcUserService(oAuth2OidcUserService)
                    .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
    }

    private void logout(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies(USER_JWT_COOKIE)
                .logoutRequestMatcher(new AntPathRequestMatcher(AppRoutes.ACCESS_LOGOUT))
                .logoutSuccessUrl(AppRoutes.ACCESS_LOGIN + "?logout").permitAll();
    }

    private void exceptionHandling(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling()
                .accessDeniedHandler(userAccessDeniedHandler);
    }

    private void filters(HttpSecurity httpSecurity) {
        httpSecurity
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
