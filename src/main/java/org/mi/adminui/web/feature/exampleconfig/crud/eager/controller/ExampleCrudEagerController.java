package org.mi.adminui.web.feature.exampleconfig.crud.eager.controller;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.mi.adminui.exception.RecordCreateException;
import org.mi.adminui.exception.RecordNotFoundException;
import org.mi.adminui.web.core.configuration.constant.AppFormMode;
import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.crud.eager.configuration.ExampleCrudEagerPageConfig;
import org.mi.adminui.web.feature.exampleconfig.crud.eager.validator.ExampleCrudEagerValidator;
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
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.SUBMIT_ERROR_MESSAGE_KEY;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.SUBMIT_ERROR_SHOW;

@Controller
public class ExampleCrudEagerController {

    private static final String PAGE_FRAGMENT_PATH = AppPages.EXAMPLE_CRUD_EAGER + " :: " + ExampleCrudEagerPageConfig.get().fragments.page;
    private static final String FORM_FRAGMENT_PATH = AppPages.EXAMPLE_CRUD_EAGER + " :: " + ExampleCrudEagerPageConfig.get().fragments.form;
    private static final String TABLE_FRAGMENT_PATH = AppPages.EXAMPLE_CRUD_EAGER + " :: " + ExampleCrudEagerPageConfig.get().fragments.table;

    private final ExampleCrudEagerValidator exampleCrudEagerValidator;
    private final ExampleConfigService exampleConfigService;

    public ExampleCrudEagerController(ExampleCrudEagerValidator exampleCrudEagerValidator,
                                      ExampleConfigService exampleConfigService) {
        this.exampleCrudEagerValidator = exampleCrudEagerValidator;
        this.exampleConfigService = exampleConfigService;
    }

    @GetMapping(AppRoutes.EXAMPLE_CRUD_EAGER)
    public String loadExampleCrudEagerPage(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudEagerPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_EAGER_CREATE);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());
        setSelectOptions(model);

        return AppPages.EXAMPLE_CRUD_EAGER;
    }

    @PostMapping(AppRoutes.EXAMPLE_CRUD_EAGER_CREATE)
    public String createExampleConfig(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, BindingResult bindingResult, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudEagerPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_EAGER_CREATE);
        setSelectOptions(model);

        exampleCrudEagerValidator.validate(exampleConfig, bindingResult);
        if (bindingResult.hasErrors()) {
            return FORM_FRAGMENT_PATH;
        }

        try {
            exampleConfigService.create(exampleConfig);
        } catch (RecordCreateException e) {
            model.addAttribute(SUBMIT_ERROR_SHOW, true);
            model.addAttribute(SUBMIT_ERROR_MESSAGE_KEY, ExampleCrudEagerPageConfig.get().submitErrorMessageKeys.errorCreating);

            return PAGE_FRAGMENT_PATH;
        }

        model.addAttribute(FORM_OBJECT, new ExampleConfig());

        return PAGE_FRAGMENT_PATH;
    }

    @PutMapping(AppRoutes.EXAMPLE_CRUD_EAGER_EDIT)
    public String editExampleConfig(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudEagerPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.EDIT);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_EAGER_EDIT_SAVE);
        model.addAttribute(FORM_OBJECT, exampleConfig);
        setSelectOptions(model);

        return FORM_FRAGMENT_PATH;
    }

    @PatchMapping(AppRoutes.EXAMPLE_CRUD_EAGER_EDIT_SAVE)
    public String saveExampleConfigEdit(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, BindingResult bindingResult, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudEagerPageConfig.get());
        setSelectOptions(model);

        exampleCrudEagerValidator.validate(exampleConfig, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(FORM_MODE, AppFormMode.EDIT);
            model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_EAGER_EDIT_SAVE);

            return FORM_FRAGMENT_PATH;
        }

        try {
            exampleConfigService.update(exampleConfig);
        } catch (RecordNotFoundException e) {
            model.addAttribute(SUBMIT_ERROR_SHOW, true);
            model.addAttribute(SUBMIT_ERROR_MESSAGE_KEY, ExampleCrudEagerPageConfig.get().submitErrorMessageKeys.errorUpdating);
        }

        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_EAGER_CREATE);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());

        return PAGE_FRAGMENT_PATH;
    }

    @GetMapping(AppRoutes.EXAMPLE_CRUD_EAGER_EDIT_CANCEL)
    public String cancelExampleConfigEdit(Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudEagerPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_EAGER_CREATE);
        model.addAttribute(FORM_OBJECT, new ExampleConfig());
        setSelectOptions(model);

        return FORM_FRAGMENT_PATH;
    }

    @DeleteMapping(AppRoutes.EXAMPLE_CRUD_EAGER_DELETE)
    public String deleteExampleConfig(@ModelAttribute(FORM_OBJECT) ExampleConfig exampleConfig, Model model) {
        model.addAttribute(PAGE_CONFIG, ExampleCrudEagerPageConfig.get());

        try {
            exampleConfigService.delete(exampleConfig.getId());
        } catch (RecordNotFoundException e) {
            model.addAttribute(SUBMIT_ERROR_SHOW, true);
            model.addAttribute(SUBMIT_ERROR_MESSAGE_KEY, ExampleCrudEagerPageConfig.get().submitErrorMessageKeys.errorDeleting);
            model.addAttribute(FORM_MODE, AppFormMode.CREATE);
            model.addAttribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_EAGER_CREATE);
            model.addAttribute(FORM_OBJECT, new ExampleConfig());
            setSelectOptions(model);

            return PAGE_FRAGMENT_PATH;
        }

        return TABLE_FRAGMENT_PATH;
    }

    private void setSelectOptions(Model model) {
        model.addAttribute(ExampleCrudEagerPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                            .collect(Collectors.toList()));
        model.addAttribute(ExampleCrudEagerPageConfig.get().selectOptions.exampleConfigType, exampleConfigService.findAllExampleConfigTypes());
    }
}