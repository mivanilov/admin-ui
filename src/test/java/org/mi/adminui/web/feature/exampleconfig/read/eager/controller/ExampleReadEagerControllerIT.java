package org.mi.adminui.web.feature.exampleconfig.read.eager.controller;

import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.read.eager.configuration.ExampleReadEagerPageConfig;

import static org.hamcrest.Matchers.containsString;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_ACTION;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_OBJECT;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.PAGE_CONFIG;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.RENDER_TABLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExampleReadEagerControllerIT extends ControllerITBase {

    @Test
    void loadExampleConfigsPage() throws Exception {
        mockMvc.perform(get(AppRoutes.EXAMPLE_READ_EAGER))
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleReadEagerPageConfig.get()))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_READ_EAGER_LOAD))
               .andExpect(model().attribute(FORM_OBJECT, new ExampleConfig()))
               .andExpect(model().attribute(ExampleReadEagerPageConfig.get().selectOptions.visibility, ExampleConfig.Visibility.values()))
               .andExpect(content().string(containsString("id=\"" + ExampleReadEagerPageConfig.get().fragments.page + "\"")));
    }

    @Test
    void loadExampleConfigs() throws Exception {
        mockMvc.perform(post(AppRoutes.EXAMPLE_READ_EAGER_LOAD))
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleReadEagerPageConfig.get()))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_READ_EAGER_LOAD))
               .andExpect(model().attribute(RENDER_TABLE, true))
               .andExpect(content().string(containsString("id=\"" + ExampleReadEagerPageConfig.get().fragments.table + "\"")));
    }
}
