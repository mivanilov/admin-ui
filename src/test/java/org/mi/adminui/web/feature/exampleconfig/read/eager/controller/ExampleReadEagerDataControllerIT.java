package org.mi.adminui.web.feature.exampleconfig.read.eager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExampleReadEagerDataControllerIT extends ControllerITBase {

    @MockBean
    private ExampleConfigService exampleConfigService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loadExampleConfigRows() throws Exception {
        ExampleConfigType exampleConfigType = new ExampleConfigType();
        exampleConfigType.setId(1L);
        exampleConfigType.setType(ExampleConfigType.Type.T1);
        exampleConfigType.setDescription("type1");

        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setName("exampleConfig");
        exampleConfig.setVisibility(ExampleConfig.Visibility.PUBLIC);
        exampleConfig.setCreateDate(LocalDate.now());
        exampleConfig.setExampleConfigType(exampleConfigType);
        exampleConfig.setActive(true);

        when(exampleConfigService.findAllExampleConfigsByVisibility(ExampleConfig.Visibility.PUBLIC)).thenReturn(List.of(exampleConfig));

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.EXAMPLE_READ_EAGER_LOAD_ROWS)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("visibility", ExampleConfig.Visibility.PUBLIC.name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(List.of(exampleConfig))));
    }
}
