package org.mi.adminui;

import org.junit.jupiter.api.BeforeEach;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.security.model.UserPrincipal;
import org.mi.adminui.security.service.CustomUserDetailsService;
import org.mi.adminui.security.util.TokenProvider;
import org.mi.adminui.testconfiguration.DataTestConfiguration;
import org.mi.adminui.util.CookieUtils;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(DataTestConfiguration.class)
public class ControllerITBase {

    protected static final String USER_NAME = "username";
    protected static final String USER_EMAIL = "user@gmail.com";
    protected static final User.RoleType USER_ROLE = User.RoleType.ADMIN;
    protected static final String JWT = "jwt";

    private UserPrincipal userPrincipal = new UserPrincipal(USER_NAME, USER_EMAIL, List.of(new SimpleGrantedAuthority(USER_ROLE.getSecurityName())));

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Mock
    protected Cookie cookie;

    @MockBean
    protected TokenProvider tokenProvider;
    @MockBean
    protected CustomUserDetailsService customUserDetailsService;
    @MockBean
    protected CookieUtils cookieUtils;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                      .apply(SecurityMockMvcConfigurers.springSecurity())
                                      .build();
        mockTokenAuthenticationFilter();
    }

    private void mockTokenAuthenticationFilter() {
        when(cookieUtils.getCookie(any(), any())).thenReturn(Optional.of(cookie));
        when(cookieUtils.deserialize(cookie, String.class)).thenReturn(JWT);

        when(tokenProvider.getUserEmailFromToken(JWT)).thenReturn(USER_EMAIL);

        when(customUserDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(userPrincipal);
    }
}
