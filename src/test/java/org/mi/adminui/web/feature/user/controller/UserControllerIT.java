package org.mi.adminui.web.feature.user.controller;

import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mi.adminui.exception.RecordCreateException;
import org.mi.adminui.exception.RecordNotFoundException;
import org.mi.adminui.web.core.configuration.constant.AppFormMode;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.user.configuration.UserPageConfig;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_ACTION;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_MODE;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.FORM_OBJECT;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.PAGE_CONFIG;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.SUBMIT_ERROR_MESSAGE_KEY;
import static org.mi.adminui.web.core.configuration.constant.AppPageParams.SUBMIT_ERROR_SHOW;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends ControllerITBase {

    private static final String EMAIL_PARAM = UserPageConfig.get().dtoFields.email;
    private static final String NAME_PARAM = UserPageConfig.get().dtoFields.name;
    private static final String ROLE_PARAM = UserPageConfig.get().dtoFields.role;

    private static final List<String> ROLE_TYPE_SELECT_OPTIONS = Arrays.stream(User.RoleType.values())
                                                                       .map(Enum::name)
                                                                       .collect(Collectors.toList());

    @MockBean
    private UserService userService;

    @Test
    void loadUsers() throws Exception {
        mockMvc.perform(get(AppRoutes.USERS))
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new User()))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.page + "\"")));
    }

    @Test
    void createUser() throws Exception {
        User user = getUser();

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.USERS_CREATE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new User()))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).create(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(user.getEmail(), argumentCaptor.getValue().getEmail()),
                () -> assertEquals(user.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(user.getRole(), argumentCaptor.getValue().getRole())
        );
    }

    @Test
    void failToCreateUserWhenInputIsInvalid() throws Exception {
        User user = getUser();
        user.setEmail("invalid");

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.USERS_CREATE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, user))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.form + "\"")));

        verify(userService, never()).create(user);
    }

    @Test
    void failToCreateUserWhenRecordAlreadyExists() throws Exception {
        User user = getUser();

        when(userService.create(user)).thenThrow(new RecordCreateException());

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.USERS_CREATE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(SUBMIT_ERROR_SHOW, true))
               .andExpect(model().attribute(SUBMIT_ERROR_MESSAGE_KEY, UserPageConfig.get().submitErrorMessageKeys.errorCreating))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).create(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(user.getEmail(), argumentCaptor.getValue().getEmail()),
                () -> assertEquals(user.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(user.getRole(), argumentCaptor.getValue().getRole())
        );
    }

    @Test
    void editUser() throws Exception {
        User user = getUser();

        MockHttpServletRequestBuilder requestBuilder = put(AppRoutes.USERS_EDIT)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.EDIT))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_EDIT_SAVE))
               .andExpect(model().attribute(FORM_OBJECT, user))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.form + "\"")));
    }

    @Test
    void saveUserEdit() throws Exception {
        User user = getUser();

        MockHttpServletRequestBuilder requestBuilder = patch(AppRoutes.USERS_EDIT_SAVE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new User()))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).update(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(user.getEmail(), argumentCaptor.getValue().getEmail()),
                () -> assertEquals(user.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(user.getRole(), argumentCaptor.getValue().getRole())
        );
    }

    @Test
    void failToSaveUserEditWhenInputIsInvalid() throws Exception {
        User user = getUser();
        user.setEmail("invalid");

        MockHttpServletRequestBuilder requestBuilder = patch(AppRoutes.USERS_EDIT_SAVE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.EDIT))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_EDIT_SAVE))
               .andExpect(model().attribute(FORM_OBJECT, user))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.form + "\"")));

        verify(userService, never()).create(user);
    }

    @Test
    void failToSaveUserEditWhenRecordNotFound() throws Exception {
        User user = getUser();

        when(userService.update(user)).thenThrow(new RecordNotFoundException());

        MockHttpServletRequestBuilder requestBuilder = patch(AppRoutes.USERS_EDIT_SAVE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new User()))
               .andExpect(model().attribute(SUBMIT_ERROR_SHOW, true))
               .andExpect(model().attribute(SUBMIT_ERROR_MESSAGE_KEY, UserPageConfig.get().submitErrorMessageKeys.errorUpdating))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).update(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(user.getEmail(), argumentCaptor.getValue().getEmail()),
                () -> assertEquals(user.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(user.getRole(), argumentCaptor.getValue().getRole())
        );
    }

    @Test
    void cancelUserEdit() throws Exception {
        mockMvc.perform(get(AppRoutes.USERS_EDIT_CANCEL))
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new User()))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.form + "\"")));
    }

    @Test
    void deleteUser() throws Exception {
        User user = getUser();
        user.setEmail("someuser@gmail.com");

        MockHttpServletRequestBuilder requestBuilder = delete(AppRoutes.USERS_DELETE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(UserPageConfig.get().selectOptions.roleType, ROLE_TYPE_SELECT_OPTIONS))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.table + "\"")));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userService).delete(argumentCaptor.capture());
        assertEquals(user.getEmail(), argumentCaptor.getValue());
    }

    @Test
    void failToDeleteUserWhenSelfDeleting() throws Exception {
        User user = getUser();

        MockHttpServletRequestBuilder requestBuilder = delete(AppRoutes.USERS_DELETE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new User()))
               .andExpect(model().attribute(SUBMIT_ERROR_SHOW, true))
               .andExpect(model().attribute(SUBMIT_ERROR_MESSAGE_KEY, UserPageConfig.get().submitErrorMessageKeys.errorDeletingSelf))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.page + "\"")));
    }

    @Test
    void failToDeleteUserWhenRecordNotFound() throws Exception {
        User user = getUser();
        user.setEmail("someuser@gmail.com");

        doThrow(new RecordNotFoundException()).when(userService).delete("someuser@gmail.com");

        MockHttpServletRequestBuilder requestBuilder = delete(AppRoutes.USERS_DELETE)
                .param(EMAIL_PARAM, user.getEmail())
                .param(NAME_PARAM, user.getName())
                .param(ROLE_PARAM, user.getRole().name());

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, UserPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.USERS_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new User()))
               .andExpect(model().attribute(SUBMIT_ERROR_SHOW, true))
               .andExpect(model().attribute(SUBMIT_ERROR_MESSAGE_KEY, UserPageConfig.get().submitErrorMessageKeys.errorDeletingNotFound))
               .andExpect(content().string(containsString("id=\"" + UserPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userService).delete(argumentCaptor.capture());
        assertEquals(user.getEmail(), argumentCaptor.getValue());
    }

    private User getUser() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setName(USER_NAME);
        user.setRole(USER_ROLE);

        return user;
    }
}
