package org.mi.adminui.web.feature.exampleconfig.read.eager.controller;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig.Visibility;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExampleReadEagerDataController {

    private final ExampleConfigService exampleConfigService;

    public ExampleReadEagerDataController(ExampleConfigService exampleConfigService) {
        this.exampleConfigService = exampleConfigService;
    }

    @PostMapping(value = AppRoutes.EXAMPLE_READ_EAGER_LOAD_ROWS,
                 consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExampleConfig> loadExampleConfigRows(@Param("visibility") Visibility visibility) {
        return exampleConfigService.findAllExampleConfigsByVisibility(visibility);
    }
}
