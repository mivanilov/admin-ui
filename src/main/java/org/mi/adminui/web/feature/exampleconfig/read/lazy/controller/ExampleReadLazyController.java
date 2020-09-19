package org.mi.adminui.web.feature.exampleconfig.read.lazy.controller;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig.Visibility;
import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.read.lazy.configuration.ExampleReadLazyPageConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_ACTION;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_OBJECT;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.PAGE_CONFIG;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.RENDER_TABLE;

@Controller
public class ExampleReadLazyController {

    private static final String TABLE_FRAGMENT_PATH = AppPages.EXAMPLE_READ_LAZY + " :: " + ExampleReadLazyPageConfig.get().fragments.table;

    @GetMapping(AppRoutes.EXAMPLE_READ_LAZY)
    public String loadExampleConfigsPage(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleReadLazyPageConfig.get());
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_READ_LAZY_LOAD);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());
        model.addAttribute(ExampleReadLazyPageConfig.get().selectOptions.visibility, Visibility.values());

        return AppPages.EXAMPLE_READ_LAZY;
    }

    @PostMapping(AppRoutes.EXAMPLE_READ_LAZY_LOAD)
    public String loadExampleConfigs(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleReadLazyPageConfig.get());
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_READ_LAZY_LOAD);
        model.addAttribute(RENDER_TABLE, true);

        return TABLE_FRAGMENT_PATH;
    }
}
