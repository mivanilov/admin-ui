package org.mi.adminui.security;

import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.security.filter.CustomUserAuthenticationFilter;
import org.mi.adminui.security.handler.CustomAccessDeniedHandler;
import org.mi.adminui.security.oauth2.CustomAuthenticationFailureHandler;
import org.mi.adminui.security.oauth2.CustomAuthenticationSuccessHandler;
import org.mi.adminui.security.oauth2.CustomAuthorizationRequestRepository;
import org.mi.adminui.security.oauth2.CustomOidcUserService;
import org.mi.adminui.security.property.AppAuthProperties;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final CustomOidcUserService customOidcUserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomUserAuthenticationFilter customUserAuthenticationFilter;
    private final AppAuthProperties appAuthProperties;

    public SecurityConfiguration(CustomAuthorizationRequestRepository customAuthorizationRequestRepository,
                                 CustomOidcUserService customOidcUserService,
                                 CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                                 CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                                 CustomAccessDeniedHandler customAccessDeniedHandler,
                                 CustomUserAuthenticationFilter customUserAuthenticationFilter,
                                 AppAuthProperties appAuthProperties) {
        this.customAuthorizationRequestRepository = customAuthorizationRequestRepository;
        this.customOidcUserService = customOidcUserService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customUserAuthenticationFilter = customUserAuthenticationFilter;
        this.appAuthProperties = appAuthProperties;
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
                             AppRoutes.ACCESS_LOGIN,
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
                .loginPage(AppRoutes.ACCESS_LOGIN)
                .authorizationEndpoint().authorizationRequestRepository(customAuthorizationRequestRepository).and()
                .userInfoEndpoint().oidcUserService(customOidcUserService).and()
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler);
    }

    private void logout(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies(appAuthProperties.getSession().getCookieName())
                .logoutUrl(AppRoutes.ACCESS_LOGOUT)
                .logoutSuccessUrl(AppRoutes.ACCESS_LOGIN + "?logout");
    }

    private void exceptionHandling(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler);
    }

    private void filters(HttpSecurity httpSecurity) {
        httpSecurity
                .addFilterBefore(customUserAuthenticationFilter, FilterSecurityInterceptor.class);
    }
}
