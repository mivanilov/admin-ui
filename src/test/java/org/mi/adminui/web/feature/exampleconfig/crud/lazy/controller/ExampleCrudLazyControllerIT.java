package org.mi.adminui.web.feature.exampleconfig.crud.lazy.controller;

import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.mi.adminui.data.feature.exampleconfig.service.ExampleConfigService;
import org.mi.adminui.exception.RecordCreateException;
import org.mi.adminui.exception.RecordNotFoundException;
import org.mi.adminui.web.core.configuration.constant.AppFormMode;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.feature.exampleconfig.crud.lazy.configuration.ExampleCrudLazyPageConfig;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
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

class ExampleCrudLazyControllerIT extends ControllerITBase {

    private static final String ID_PARAM = ExampleCrudLazyPageConfig.get().dtoFields.id;
    private static final String NAME_PARAM = ExampleCrudLazyPageConfig.get().dtoFields.name;
    private static final String VISIBILITY_PARAM = ExampleCrudLazyPageConfig.get().dtoFields.visibility;
    private static final String CREATE_DATE_PARAM = ExampleCrudLazyPageConfig.get().dtoFields.createDate;
    private static final String TYPE_PARAM = ExampleCrudLazyPageConfig.get().dtoFields.exampleConfigTypeValue;
    private static final String ACTIVE_PARAM = ExampleCrudLazyPageConfig.get().dtoFields.active;

    @MockBean
    private ExampleConfigService exampleConfigService;

    @Test
    void loadExampleCrudLazyPage() throws Exception {
        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));

        mockMvc.perform(get(AppRoutes.EXAMPLE_CRUD_LAZY))
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new ExampleConfig()))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.page + "\"")));
    }

    @Test
    void createExampleConfig() throws Exception {
        ExampleConfig exampleConfig = getExampleConfig();

        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.EXAMPLE_CRUD_LAZY_CREATE)
                .param(NAME_PARAM, exampleConfig.getName())
                .param(VISIBILITY_PARAM, exampleConfig.getVisibility().name())
                .param(CREATE_DATE_PARAM, exampleConfig.getCreateDate().toString())
                .param(TYPE_PARAM, exampleConfig.getExampleConfigTypeValue())
                .param(ACTIVE_PARAM, String.valueOf(exampleConfig.isActive()));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new ExampleConfig()))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<ExampleConfig> argumentCaptor = ArgumentCaptor.forClass(ExampleConfig.class);
        verify(exampleConfigService).create(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(exampleConfig.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(exampleConfig.getVisibility(), argumentCaptor.getValue().getVisibility()),
                () -> assertEquals(exampleConfig.getCreateDate(), argumentCaptor.getValue().getCreateDate()),
                () -> assertEquals(exampleConfig.getExampleConfigTypeValue(), argumentCaptor.getValue().getExampleConfigTypeValue()),
                () -> assertEquals(exampleConfig.isActive(), argumentCaptor.getValue().isActive())
        );
    }

    @Test
    void failToCreateExampleConfigWhenInputIsInvalid() throws Exception {
        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setName("invalid");

        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.EXAMPLE_CRUD_LAZY_CREATE)
                .param(NAME_PARAM, exampleConfig.getName())
                .param(VISIBILITY_PARAM, String.valueOf(exampleConfig.getVisibility()))
                .param(CREATE_DATE_PARAM, String.valueOf(exampleConfig.getCreateDate()))
                .param(TYPE_PARAM, String.valueOf(exampleConfig.getExampleConfigTypeValue()))
                .param(ACTIVE_PARAM, String.valueOf(exampleConfig.isActive()));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, exampleConfig))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.form + "\"")));

        verify(exampleConfigService, never()).create(exampleConfig);
    }

    @Test
    void failToCreateExampleConfigWhenRecordAlreadyExists() throws Exception {
        ExampleConfig exampleConfig = getExampleConfig();
        exampleConfig.getExampleConfigType().setType(null);
        exampleConfig.getExampleConfigType().setDescription(null);

        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));
        when(exampleConfigService.create(exampleConfig)).thenThrow(new RecordCreateException());

        MockHttpServletRequestBuilder requestBuilder = post(AppRoutes.EXAMPLE_CRUD_LAZY_CREATE)
                .param(NAME_PARAM, exampleConfig.getName())
                .param(VISIBILITY_PARAM, String.valueOf(exampleConfig.getVisibility()))
                .param(CREATE_DATE_PARAM, String.valueOf(exampleConfig.getCreateDate()))
                .param(TYPE_PARAM, String.valueOf(exampleConfig.getExampleConfigTypeValue()))
                .param(ACTIVE_PARAM, String.valueOf(exampleConfig.isActive()));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, exampleConfig))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(model().attribute(SUBMIT_ERROR_MESSAGE_KEY, ExampleCrudLazyPageConfig.get().submitErrorMessageKeys.errorCreating))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.form + "\"")));

        ArgumentCaptor<ExampleConfig> argumentCaptor = ArgumentCaptor.forClass(ExampleConfig.class);
        verify(exampleConfigService).create(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(exampleConfig.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(exampleConfig.getVisibility(), argumentCaptor.getValue().getVisibility()),
                () -> assertEquals(exampleConfig.getCreateDate(), argumentCaptor.getValue().getCreateDate()),
                () -> assertEquals(exampleConfig.getExampleConfigTypeValue(), argumentCaptor.getValue().getExampleConfigTypeValue()),
                () -> assertEquals(exampleConfig.isActive(), argumentCaptor.getValue().isActive())
        );
    }

    @Test
    void editExampleConfig() throws Exception {
        ExampleConfig exampleConfig = getExampleConfig();
        exampleConfig.setId(1L);
        ExampleConfigType exampleConfigType = new ExampleConfigType();
        exampleConfigType.setId(1L);
        exampleConfig.setExampleConfigType(exampleConfigType);

        ExampleConfigType exampleConfigTypeSelectOption = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigTypeSelectOption));

        MockHttpServletRequestBuilder requestBuilder = put(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT)
                .param(ID_PARAM, String.valueOf(exampleConfig.getId()))
                .param(NAME_PARAM, exampleConfig.getName())
                .param(VISIBILITY_PARAM, String.valueOf(exampleConfig.getVisibility()))
                .param(CREATE_DATE_PARAM, String.valueOf(exampleConfig.getCreateDate()))
                .param(TYPE_PARAM, String.valueOf(exampleConfig.getExampleConfigTypeValue()))
                .param(ACTIVE_PARAM, String.valueOf(exampleConfig.isActive()));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.EDIT))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE))
               .andExpect(model().attribute(FORM_OBJECT, exampleConfig))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigTypeSelectOption)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.form + "\"")));
    }

    @Test
    void saveExampleConfigEdit() throws Exception {
        ExampleConfig exampleConfig = getExampleConfig();
        exampleConfig.setId(1L);

        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));

        MockHttpServletRequestBuilder requestBuilder = patch(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE)
                .param(ID_PARAM, exampleConfig.getId().toString())
                .param(NAME_PARAM, exampleConfig.getName())
                .param(VISIBILITY_PARAM, exampleConfig.getVisibility().name())
                .param(CREATE_DATE_PARAM, exampleConfig.getCreateDate().toString())
                .param(TYPE_PARAM, exampleConfig.getExampleConfigTypeValue())
                .param(ACTIVE_PARAM, String.valueOf(exampleConfig.isActive()));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new ExampleConfig()))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<ExampleConfig> argumentCaptor = ArgumentCaptor.forClass(ExampleConfig.class);
        verify(exampleConfigService).update(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(exampleConfig.getId(), argumentCaptor.getValue().getId()),
                () -> assertEquals(exampleConfig.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(exampleConfig.getVisibility(), argumentCaptor.getValue().getVisibility()),
                () -> assertEquals(exampleConfig.getCreateDate(), argumentCaptor.getValue().getCreateDate()),
                () -> assertEquals(exampleConfig.getExampleConfigTypeValue(), argumentCaptor.getValue().getExampleConfigTypeValue()),
                () -> assertEquals(exampleConfig.isActive(), argumentCaptor.getValue().isActive())
        );
    }

    @Test
    void failToSaveExampleConfigEditWhenInputIsInvalid() throws Exception {
        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setId(1L);
        exampleConfig.setName("invalid");

        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));

        MockHttpServletRequestBuilder requestBuilder = patch(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE)
                .param(ID_PARAM, exampleConfig.getId().toString())
                .param(NAME_PARAM, exampleConfig.getName())
                .param(VISIBILITY_PARAM, String.valueOf(exampleConfig.getVisibility()))
                .param(CREATE_DATE_PARAM, String.valueOf(exampleConfig.getCreateDate()))
                .param(TYPE_PARAM, String.valueOf(exampleConfig.getExampleConfigTypeValue()))
                .param(ACTIVE_PARAM, String.valueOf(exampleConfig.isActive()));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.EDIT))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE))
               .andExpect(model().attribute(FORM_OBJECT, exampleConfig))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.form + "\"")));

        verify(exampleConfigService, never()).update(exampleConfig);
    }

    @Test
    void failToSaveExampleConfigEditWhenRecordNotFound() throws Exception {
        ExampleConfig exampleConfig = getExampleConfig();
        exampleConfig.setId(1L);
        exampleConfig.getExampleConfigType().setType(null);
        exampleConfig.getExampleConfigType().setDescription(null);

        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));
        when(exampleConfigService.update(exampleConfig)).thenThrow(new RecordNotFoundException());

        MockHttpServletRequestBuilder requestBuilder = patch(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_SAVE)
                .param(ID_PARAM, exampleConfig.getId().toString())
                .param(NAME_PARAM, exampleConfig.getName())
                .param(VISIBILITY_PARAM, String.valueOf(exampleConfig.getVisibility()))
                .param(CREATE_DATE_PARAM, String.valueOf(exampleConfig.getCreateDate()))
                .param(TYPE_PARAM, String.valueOf(exampleConfig.getExampleConfigTypeValue()))
                .param(ACTIVE_PARAM, String.valueOf(exampleConfig.isActive()));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new ExampleConfig()))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(model().attribute(SUBMIT_ERROR_MESSAGE_KEY, ExampleCrudLazyPageConfig.get().submitErrorMessageKeys.errorUpdating))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<ExampleConfig> argumentCaptor = ArgumentCaptor.forClass(ExampleConfig.class);
        verify(exampleConfigService).update(argumentCaptor.capture());
        assertAll(
                () -> assertEquals(exampleConfig.getId(), argumentCaptor.getValue().getId()),
                () -> assertEquals(exampleConfig.getName(), argumentCaptor.getValue().getName()),
                () -> assertEquals(exampleConfig.getVisibility(), argumentCaptor.getValue().getVisibility()),
                () -> assertEquals(exampleConfig.getCreateDate(), argumentCaptor.getValue().getCreateDate()),
                () -> assertEquals(exampleConfig.getExampleConfigTypeValue(), argumentCaptor.getValue().getExampleConfigTypeValue()),
                () -> assertEquals(exampleConfig.isActive(), argumentCaptor.getValue().isActive())
        );
    }

    @Test
    void cancelExampleConfigEdit() throws Exception {
        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));

        mockMvc.perform(get(AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_CANCEL))
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new ExampleConfig()))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.form + "\"")));
    }

    @Test
    void deleteExampleConfig() throws Exception {
        String id = "1";

        MockHttpServletRequestBuilder requestBuilder = delete(AppRoutes.EXAMPLE_CRUD_LAZY_DELETE)
                .param(ID_PARAM, id);

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.table + "\"")));

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(exampleConfigService).delete(argumentCaptor.capture());
        assertEquals(Long.parseLong(id), argumentCaptor.getValue());
    }

    @Test
    void failToDeleteExampleConfigWhenRecordNotFound() throws Exception {
        String id = "1";

        ExampleConfigType exampleConfigType = getExampleConfigType();
        when(exampleConfigService.findAllExampleConfigTypes()).thenReturn(List.of(exampleConfigType));
        doThrow(new RecordNotFoundException()).when(exampleConfigService).delete(Long.parseLong(id));

        MockHttpServletRequestBuilder requestBuilder = delete(AppRoutes.EXAMPLE_CRUD_LAZY_DELETE)
                .param(ID_PARAM, id);

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(model().attribute(PAGE_CONFIG, ExampleCrudLazyPageConfig.get()))
               .andExpect(model().attribute(SUBMIT_ERROR_MESSAGE_KEY, ExampleCrudLazyPageConfig.get().submitErrorMessageKeys.errorDeleting))
               .andExpect(model().attribute(FORM_MODE, AppFormMode.CREATE))
               .andExpect(model().attribute(FORM_ACTION, AppRoutes.EXAMPLE_CRUD_LAZY_CREATE))
               .andExpect(model().attribute(FORM_OBJECT, new ExampleConfig()))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.visibility, Arrays.stream(ExampleConfig.Visibility.values())
                                                                                                            .map(ExampleConfig.Visibility::name)
                                                                                                            .collect(Collectors.toList())))
               .andExpect(model().attribute(ExampleCrudLazyPageConfig.get().selectOptions.exampleConfigType, List.of(exampleConfigType)))
               .andExpect(content().string(containsString("id=\"" + ExampleCrudLazyPageConfig.get().fragments.page + "\"")));

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(exampleConfigService).delete(argumentCaptor.capture());
        assertEquals(Long.parseLong(id), argumentCaptor.getValue());
    }

    private ExampleConfig getExampleConfig() {
        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setName("exampleConfig");
        exampleConfig.setVisibility(ExampleConfig.Visibility.PUBLIC);
        exampleConfig.setCreateDate(LocalDate.now());
        exampleConfig.setExampleConfigType(getExampleConfigType());
        exampleConfig.setActive(true);

        return exampleConfig;
    }

    private ExampleConfigType getExampleConfigType() {
        ExampleConfigType exampleConfigType = new ExampleConfigType();
        exampleConfigType.setId(1L);
        exampleConfigType.setType(ExampleConfigType.Type.T1);
        exampleConfigType.setDescription("type1");

        return exampleConfigType;
    }
}
