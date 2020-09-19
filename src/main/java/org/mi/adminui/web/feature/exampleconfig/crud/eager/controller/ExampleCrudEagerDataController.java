package org.mi.adminui.web.feature.exampleconfig.crud.eager.controller;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExampleCrudEagerDataController {

    private final ExampleConfigService exampleConfigService;

    public ExampleCrudEagerDataController(ExampleConfigService exampleConfigService) {
        this.exampleConfigService = exampleConfigService;
    }

    @PostMapping(value = AppRoutes.EXAMPLE_CRUD_EAGER_LOAD_ROWS,
                 consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExampleConfig> loadExampleConfigRows() {
        return exampleConfigService.findAll();
    }
}
