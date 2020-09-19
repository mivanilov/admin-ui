package org.mi.adminui.web.feature.exampleconfig.read.eager.controller;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig.Visibility;
import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.read.eager.configuration.ExampleReadEagerPageConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_ACTION;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_OBJECT;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.PAGE_CONFIG;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.RENDER_TABLE;

@Controller
public class ExampleReadEagerController {

    private static final String TABLE_FRAGMENT_PATH = AppPages.EXAMPLE_READ_EAGER + " :: " + ExampleReadEagerPageConfig.get().fragments.table;

    @GetMapping(AppRoutes.EXAMPLE_READ_EAGER)
    public String loadExampleConfigsPage(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleReadEagerPageConfig.get());
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_READ_EAGER_LOAD);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());
        model.addAttribute(ExampleReadEagerPageConfig.get().selectOptions.visibility, Visibility.values());

        return AppPages.EXAMPLE_READ_EAGER;
    }

    @PostMapping(AppRoutes.EXAMPLE_READ_EAGER_LOAD)
    public String loadExampleConfigs(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleReadEagerPageConfig.get());
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_READ_EAGER_LOAD);
        model.addAttribute(RENDER_TABLE, true);

        return TABLE_FRAGMENT_PATH;
    }
}
