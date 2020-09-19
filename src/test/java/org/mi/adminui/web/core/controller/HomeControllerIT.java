package org.mi.adminui.web.core.controller;

import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeControllerIT extends ControllerITBase {

    @Test
    void loadHomePage() throws Exception {
        mockMvc.perform(get(AppRoutes.HOME))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("id=\"home\"")));
    }
}
