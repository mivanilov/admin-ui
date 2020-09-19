package org.mi.adminui.web.core.controller;

import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.mi.adminui.web.core.configuration.constant.AppSecurity.TOKEN;
import static org.mi.adminui.web.core.configuration.constant.AppSecurity.USER_JWT_COOKIE;
import static org.mi.adminui.web.core.controller.AccessController.AUTH_URL;
import static org.mi.adminui.web.core.controller.AccessController.GOOGLE_AUTH_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccessControllerIT extends ControllerITBase {

    private static final String TOKEN_VALUE = "TOKEN_VALUE";
    private static final String SERIALIZED_TOKEN_VALUE = "SERIALIZED_TOKEN_VALUE";

    @Test
    void login() throws Exception {
        mockMvc.perform(get(AppRoutes.ACCESS_LOGIN))
               .andExpect(status().isOk())
               .andExpect(model().attribute(AUTH_URL, GOOGLE_AUTH_URL))
               .andExpect(content().string(containsString("id=\"login\"")));
    }

    @Test
    void denied() throws Exception {
        mockMvc.perform(get(AppRoutes.ACCESS_DENIED))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("id=\"accessDenied\"")));
    }

    @Test
    void loginSuccess() throws Exception {
        when(cookieUtils.serialize(TOKEN_VALUE)).thenReturn(SERIALIZED_TOKEN_VALUE);

        MockHttpServletRequestBuilder requestBuilder = get(AppRoutes.ACCESS_LOGIN_SUCCESS)
                .param(TOKEN, TOKEN_VALUE);

        mockMvc.perform(requestBuilder)
               .andExpect(status().is3xxRedirection());

        verify(cookieUtils).addCookie(any(), eq(USER_JWT_COOKIE), eq(SERIALIZED_TOKEN_VALUE), eq(86400));
    }
}
