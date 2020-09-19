package org.mi.adminui.web.feature.exampleconfig.crud.lazy.controller;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.mi.adminui.web.core.configuration.constant.AppFormMode;
import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.crud.eager.configuration.ExampleCrudEagerPageConfig;
import org.mi.adminui.web.feature.exampleconfig.crud.lazy.configuration.ExampleCrudLazyPageConfig;
import org.mi.adminui.web.feature.exampleconfig.crud.lazy.validator.ExampleCrudLazyValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_ACTION;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_MODE;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_OBJECT;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.PAGE_CONFIG;

@Controller
public class ExampleCrudLazyController {

    private static final String PAGE_FRAGMENT_PATH = AppPages.EXAMPLE_CRUD_LAZY + " :: " + ExampleCrudLazyPageConfig.get().fragments.page;
    private static final String FORM_FRAGMENT_PATH = AppPages.EXAMPLE_CRUD_LAZY + " :: " + ExampleCrudLazyPageConfig.get().fragments.form;
    private static final String TABLE_FRAGMENT_PATH = AppPages.EXAMPLE_CRUD_LAZY + " :: " + ExampleCrudLazyPageConfig.get().fragments.table;

    private final ExampleCrudLazyValidator exampleCrudLazyValidator;
    private final ExampleConfigService exampleConfigService;

    public ExampleCrudLazyController(ExampleCrudLazyValidator exampleCrudLazyValidator,
                                     ExampleConfigService exampleConfigService) {
        this.exampleCrudLazyValidator = exampleCrudLazyValidator;
        this.exampleConfigService = exampleConfigService;
    }

    @GetMapping(AppRoutes.EXAMPLE_CRUD_LAZY)
    public String loadExampleCrudLazyPage(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());
        setSelectOptions(model);

        return AppPages.EXAMPLE_CRUD_LAZY;
    }

    @PostMapping(AppRoutes.EXAMPLE_CRUD_LAZY_CREATE)
    public String createExampleConfig(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, BindingResult bindingResult, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE);
        setSelectOptions(model);

        exampleCrudLazyValidator.validate(exampleConfig, bindingResult);
        if (bindingResult.hasErrors()) {
            return FORM_FRAGMENT_PATH;
        }

        exampleConfigService.create(exampleConfig);

        model.addAttribute(FORM_OBJECT, new ExampleConfig());

        return PAGE_FRAGMENT_PATH;
    }

    @PutMapping(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT)
    public String editExampleConfig(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.EDIT);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE);
        model.addAttribute(FORM_OBJECT, exampleConfig);
        setSelectOptions(model);

        return FORM_FRAGMENT_PATH;
    }

    @PatchMapping(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE)
    public String saveExampleConfigEdit(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, BindingResult bindingResult, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get());
        setSelectOptions(model);

        exampleCrudLazyValidator.validate(exampleConfig, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(FORM_MODE, AppFormMode.EDIT);
            model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE);

            return FORM_FRAGMENT_PATH;
        }

        exampleConfigService.update(exampleConfig);

        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());

        return PAGE_FRAGMENT_PATH;
    }

    @GetMapping(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_CANCEL)
    public String cancelExampleConfigEdit(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());
        setSelectOptions(model);

        return FORM_FRAGMENT_PATH;
    }

    @DeleteMapping(AppRoutes.EXAMPLE_CRUD_LAZY_DELETE)
    public String deleteExampleConfig(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get());

        exampleConfigService.delete(exampleConfig.getId());

        return TABLE_FRAGMENT_PATH;
    }

    private void setSelectOptions(Model model) {
        model.addAttribute(ExampleCrudEagerPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                            .collect(Collectors.toList()));
        model.addAttribute(ExampleCrudEagerPageConfig.get().selectOptions.exampleConfigType, exampleConfigService.findAllExampleConfigTypes());
    }
}