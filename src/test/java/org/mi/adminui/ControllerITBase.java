package org.mi.adminui;

import org.junit.jupiter.api.BeforeEach;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.security.property.AppAuthProperties;
import org.mi.adminui.security.userdetails.CustomUserDetails;
import org.mi.adminui.security.userdetails.CustomUserDetailsService;
import org.mi.adminui.security.util.CookieUtils;
import org.mi.adminui.security.util.CryptoUtils;
import org.mi.adminui.testconfiguration.DataTestConfiguration;
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
    protected static final String ENCRYPTED_USER_EMAIL = "encryptedUserEmail";

    private final CustomUserDetails customUserDetails = new CustomUserDetails(USER_NAME, USER_EMAIL, List.of(new SimpleGrantedAuthority(USER_ROLE.getSecurityName())));

    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected AppAuthProperties authProperties;

    @Mock
    protected Cookie cookie;

    @MockBean
    protected CustomUserDetailsService customUserDetailsService;
    @MockBean
    protected CookieUtils cookieUtils;
    @MockBean
    protected CryptoUtils cryptoUtils;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                      .apply(SecurityMockMvcConfigurers.springSecurity())
                                      .build();
        mockCustomUserAuthenticationFilter();
    }

    private void mockCustomUserAuthenticationFilter() {
        when(cookieUtils.getCookie(any(), any())).thenReturn(Optional.of(cookie));
        when(cookie.getValue()).thenReturn(ENCRYPTED_USER_EMAIL);
        when(cryptoUtils.decrypt(ENCRYPTED_USER_EMAIL, authProperties.getSession().getSecret())).thenReturn(USER_EMAIL);
        when(customUserDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(customUserDetails);
    }
}
