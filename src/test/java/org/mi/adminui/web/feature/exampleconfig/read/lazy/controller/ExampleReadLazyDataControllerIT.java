package org.mi.adminui.web.feature.exampleconfig.read.lazy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.core.model.ExampleConfigPageRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExampleReadLazyDataControllerIT extends ControllerITBase {

    @MockBean
    private ExampleConfigService exampleConfigService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loadExampleConfigRowsPage() throws Exception {
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

        when(exampleConfigService.findPageOfExampleConfigsByVisibility(ExampleConfig.Visibility.PRIVATE, 2, 1)).thenReturn(new PageImpl(List.of(exampleConfig)));

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.EXAMPLE_READ_LAZY_LOAD_ROWS_PAGE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("visibility", ExampleConfig.Visibility.PRIVATE.name())
                .param("pageNumber", "3")
                .param("pageSize", "1");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(new ExampleConfigPageRes(1L, List.of(exampleConfig)))));
    }
}
