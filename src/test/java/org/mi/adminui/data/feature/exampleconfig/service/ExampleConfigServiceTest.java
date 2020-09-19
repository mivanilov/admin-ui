package org.mi.adminui.data.feature.exampleconfig.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.data.core.exception.RecordCreateException;
import org.mi.adminui.data.core.exception.RecordNotFoundException;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.mi.adminui.data.feature.exampleconfig.repository.ExampleConfigRepository;
import org.mi.adminui.data.feature.exampleconfig.repository.ExampleConfigTypeRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExampleConfigServiceTest {

    @Mock
    private ExampleConfigRepository exampleConfigRepository;

    @Mock
    private ExampleConfigTypeRepository exampleConfigTypeRepository;

    private ExampleConfigService exampleConfigService;

    @BeforeEach
    void setUp() {
        exampleConfigService = new ExampleConfigService(exampleConfigRepository, exampleConfigTypeRepository);
    }

    @Test
    void findAllExampleConfigs() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.findAll()).thenReturn(List.of(exampleConfig));

        List<ExampleConfig> exampleConfigs = exampleConfigService.findAll();

        assertEquals(List.of(exampleConfig), exampleConfigs);
    }

    @Test
    void findPageOfExampleConfigs() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.findAll(PageRequest.of(0, 1))).thenReturn(new PageImpl(List.of(exampleConfig)));

        Page<ExampleConfig> exampleConfigPage = exampleConfigService.findPageOfExampleConfigs(0, 1);

        assertNotNull(exampleConfigPage);
        assertNotNull(exampleConfigPage.getContent());
        assertEquals(List.of(exampleConfig), exampleConfigPage.getContent());
    }

    @Test
    void findExampleConfigById() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.findById(exampleConfig.getId())).thenReturn(Optional.of(exampleConfig));

        Optional<ExampleConfig> foundExampleConfig = exampleConfigService.find(exampleConfig.getId());

        assertTrue(foundExampleConfig.isPresent());
        assertEquals(exampleConfig, foundExampleConfig.get());
    }

    @Test
    void exampleConfigNotFound() {
        when(exampleConfigRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ExampleConfig> noExampleConfig = exampleConfigService.find(1L);

        assertTrue(noExampleConfig.isEmpty());
    }

    @Test
    void findAllExampleConfigsByVisibility() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.findByVisibility(ExampleConfig.Visibility.PUBLIC, Pageable.unpaged())).thenReturn(new PageImpl(List.of(exampleConfig)));

        List<ExampleConfig> foundExampleConfigs = exampleConfigService.findAllExampleConfigsByVisibility(ExampleConfig.Visibility.PUBLIC);

        assertNotNull(foundExampleConfigs);
        assertEquals(List.of(exampleConfig), foundExampleConfigs);
    }

    @Test
    void findPageOfExampleConfigsByVisibility() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.findByVisibility(ExampleConfig.Visibility.PUBLIC, PageRequest.of(1, 1))).thenReturn(new PageImpl(List.of(exampleConfig)));

        Page<ExampleConfig> foundExampleConfigPage = exampleConfigService.findPageOfExampleConfigsByVisibility(ExampleConfig.Visibility.PUBLIC, 1, 1);

        assertNotNull(foundExampleConfigPage);
        assertNotNull(foundExampleConfigPage.getContent());
        assertEquals(List.of(exampleConfig), foundExampleConfigPage.getContent());
    }

    @Test
    void createExampleConfig() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.existsById(exampleConfig.getId())).thenReturn(false);
        when(exampleConfigRepository.save(exampleConfig)).thenReturn(exampleConfig);

        ExampleConfig createdExampleConfig = exampleConfigService.create(exampleConfig);

        assertEquals(exampleConfig, createdExampleConfig);
    }

    @Test
    void exampleConfigToCreateAlreadyExists() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.existsById(exampleConfig.getId())).thenReturn(true);

        RecordCreateException exception = assertThrows(RecordCreateException.class, () -> exampleConfigService.create(exampleConfig));

        assertEquals("Record to insert already exists", exception.getMessage());
        verify(exampleConfigRepository, never()).save(exampleConfig);
    }

    @Test
    void updateExampleConfig() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.existsById(exampleConfig.getId())).thenReturn(true);
        when(exampleConfigRepository.save(exampleConfig)).thenReturn(exampleConfig);

        ExampleConfig updatedExampleConfig = exampleConfigService.update(exampleConfig);

        assertEquals(exampleConfig, updatedExampleConfig);
    }

    @Test
    void exampleConfigToUpdateNotFound() {
        ExampleConfig exampleConfig = getExampleConfig();

        when(exampleConfigRepository.existsById(exampleConfig.getId())).thenReturn(false);

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> exampleConfigService.update(exampleConfig));

        assertEquals("Record to update not found", exception.getMessage());
        verify(exampleConfigRepository, never()).save(exampleConfig);
    }

    @Test
    void deleteExampleConfig() {
        ExampleConfig exampleConfig = getExampleConfig();

        exampleConfigService.delete(exampleConfig.getId());

        verify(exampleConfigRepository).deleteById(exampleConfig.getId());
    }

    @Test
    void exampleConfigToDeleteNotFound() {
        ExampleConfig exampleConfig = getExampleConfig();

        doThrow(new EmptyResultDataAccessException(1)).when(exampleConfigRepository).deleteById(exampleConfig.getId());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> exampleConfigService.delete(exampleConfig.getId()));

        assertEquals("Record to delete not found", exception.getMessage());
    }

    @Test
    void findAllExampleConfigTypes() {
        ExampleConfigType exampleConfigType = getExampleConfigType();

        when(exampleConfigTypeRepository.findAll()).thenReturn(List.of(exampleConfigType));

        List<ExampleConfigType> exampleConfigTypes = exampleConfigService.findAllExampleConfigTypes();

        assertEquals(List.of(exampleConfigType), exampleConfigTypes);
    }

    private ExampleConfig getExampleConfig() {
        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setId(1L);
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
