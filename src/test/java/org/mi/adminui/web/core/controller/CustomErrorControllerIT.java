package org.mi.adminui.web.core.controller;

import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomErrorControllerIT extends ControllerITBase {

    private static final String ERROR_MSG = "errorMsg";
    private static final String ERROR_UNKNOWN = "error.unknown";
    private static final String ERROR_404 = "error.404";
    private static final String ERROR_PAGE_ID = "error";

    @Test
    void handleUnknownError() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(AppRoutes.ERROR)
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500);

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(ERROR_MSG, ERROR_UNKNOWN))
               .andExpect(content().string(containsString("id=\"" + ERROR_PAGE_ID + "\"")));
    }

    @Test
    void handle404Error() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(AppRoutes.ERROR)
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404);

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(ERROR_MSG, ERROR_404))
               .andExpect(content().string(containsString("id=\"" + ERROR_PAGE_ID + "\"")));
    }
}
