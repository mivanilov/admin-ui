package org.mi.adminui.web.feature.exampleconfig.read.lazy.controller;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.core.model.ExampleConfigPageReq;
import org.mi.adminui.web.feature.exampleconfig.core.model.ExampleConfigPageRes;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleReadLazyDataController {

    private final ExampleConfigService exampleConfigService;

    public ExampleReadLazyDataController(ExampleConfigService exampleConfigService) {
        this.exampleConfigService = exampleConfigService;
    }

    @PostMapping(value = AppRoutes.EXAMPLE_READ_LAZY_LOAD_ROWS_PAGE,
                 consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ExampleConfigPageRes loadExampleConfigRowsPage(ExampleConfigPageReq exampleConfigPageReq) {
        Page<ExampleConfig> exampleConfigPage = exampleConfigService.findPageOfExampleConfigsByVisibility(exampleConfigPageReq.getVisibility(),
                                                                                                          exampleConfigPageReq.getPageNumber() - 1,
                                                                                                          exampleConfigPageReq.getPageSize());
        return new ExampleConfigPageRes(exampleConfigPage.getTotalElements(), exampleConfigPage.getContent());
    }
}
