package org.mi.adminui.web.feature.user.controller;

import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mi.adminui.security.userdetails.CustomUserDetails;
import org.mi.adminui.security.authentication.AuthenticationFacade;
import org.mi.adminui.web.core.configuration.constant.AppFormMode;
import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.user.configuration.UserPageConfig;
import org.mi.adminui.web.feature.user.validator.UserValidator;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_ACTION;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_MODE;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_OBJECT;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.PAGE_CONFIG;

@Controller
public class UserController {

    private static final String PAGE_FRAGMENT_PATH = AppPages.USERS + " :: " + UserPageConfig.get().fragments.page;
    private static final String FORM_FRAGMENT_PATH = AppPages.USERS + " :: " + UserPageConfig.get().fragments.form;
    private static final String TABLE_FRAGMENT_PATH = AppPages.USERS + " :: " + UserPageConfig.get().fragments.table;

    private static final List<String> ROLE_TYPE_SELECT_OPTIONS = Arrays.stream(User.RoleType.values())
                                                                       .map(Enum::name)
                                                                       .collect(Collectors.toList());

    private final UserService userService;
    private final UserValidator userValidator;
    private final AuthenticationFacade authenticationFacade;

    public UserController(UserService userService,
                          UserValidator userValidator,
                          AuthenticationFacade authenticationFacade) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping(AppRoutes.USERS)
    public String loadUsers(Model model) {
        model.addAttribute(PAGE_CONFIG, UserPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.USERS_CREATE);
        model.addAttribute(FORM_OBJECT, new User());
        model.addAttribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS);

        return AppPages.USERS;
    }

    @PostMapping(AppRoutes.USERS_CREATE)
    public String createUser(@ModelAttribute(FORM_OBJECT) User user, BindingResult bindingResult, Model model) {
        model.addAttribute(PAGE_CONFIG, UserPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.USERS_CREATE);
        model.addAttribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS);

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return FORM_FRAGMENT_PATH;
        }

        userService.create(user);

        model.addAttribute(FORM_OBJECT, new User());

        return PAGE_FRAGMENT_PATH;
    }

    @PutMapping(AppRoutes.USERS_EDIT)
    public String editUser(@ModelAttribute(FORM_OBJECT) User user, Model model) {
        model.addAttribute(PAGE_CONFIG, UserPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.EDIT);
        model.addAttribute(FORM_ACTION, AppRoutes.USERS_EDIT_SAVE);
        model.addAttribute(FORM_OBJECT, user);
        model.addAttribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS);

        return FORM_FRAGMENT_PATH;
    }

    @PatchMapping(AppRoutes.USERS_EDIT_SAVE)
    public String saveUserEdit(@ModelAttribute(FORM_OBJECT) User user, BindingResult bindingResult, Model model) {
        model.addAttribute(PAGE_CONFIG, UserPageConfig.get());
        model.addAttribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS);

        userValidator.validateEdit(user, bindingResult, getLoggedInUserPrincipal(authenticationFacade));
        if (bindingResult.hasErrors()) {
            model.addAttribute(FORM_MODE, AppFormMode.EDIT);
            model.addAttribute(FORM_ACTION, AppRoutes.USERS_EDIT_SAVE);

            return FORM_FRAGMENT_PATH;
        }

        userService.update(user);

        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.USERS_CREATE);
        model.addAttribute(FORM_OBJECT, new User());

        return PAGE_FRAGMENT_PATH;
    }

    @GetMapping(AppRoutes.USERS_EDIT_CANCEL)
    public String cancelUserEdit(Model model) {
        model.addAttribute(PAGE_CONFIG, UserPageConfig.get());
        model.addAttribute(FORM_MODE, AppFormMode.CREATE);
        model.addAttribute(FORM_ACTION, AppRoutes.USERS_CREATE);
        model.addAttribute(FORM_OBJECT, new User());
        model.addAttribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS);

        return FORM_FRAGMENT_PATH;
    }

    @DeleteMapping(AppRoutes.USERS_DELETE)
    public String deleteUser(@ModelAttribute(FORM_OBJECT) User user, BindingResult bindingResult, Model model) {
        model.addAttribute(PAGE_CONFIG, UserPageConfig.get());

        userValidator.validateDelete(user, bindingResult, getLoggedInUserPrincipal(authenticationFacade));
        if (!bindingResult.hasErrors()) {
            userService.delete(user.getId());
        }

        return TABLE_FRAGMENT_PATH;
    }

    private static CustomUserDetails getLoggedInUserPrincipal(AuthenticationFacade authenticationFacade) {
        return (CustomUserDetails) authenticationFacade.getAuthentication().getPrincipal();
    }
}