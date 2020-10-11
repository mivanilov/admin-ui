package org.mi.adminui.web.core.controller;

import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccessControllerIT extends ControllerITBase {

    private static final String AUTH_URL = "authUrl";
    private static final String GOOGLE_AUTH_URL = "/oauth2/authorization/google";

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
}
