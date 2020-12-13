package org.mi.adminui.data.feature.exampleconfig.repository;

import org.junit.jupiter.api.Test;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExampleConfigRepositoryIT {

    @Autowired
    private ExampleConfigRepository exampleConfigRepository;

    @Autowired
    private ExampleConfigTypeRepository exampleConfigTypeRepository;

    @Test
    void findAllExampleConfigs() {
        ExampleConfig exampleConfig1 = insertExampleConfig("exampleConfig1", ExampleConfig.Visibility.PRIVATE, LocalDate.now(), true,
                                                           insertExampleConfigType(ExampleConfigType.Type.T1, "type1"));
        ExampleConfig exampleConfig2 = insertExampleConfig("exampleConfig2", ExampleConfig.Visibility.PUBLIC, LocalDate.now(), false,
                                                           insertExampleConfigType(ExampleConfigType.Type.T2, "type2"));

        List<ExampleConfig> exampleConfigs = exampleConfigRepository.findAll();

        assertNotNull(exampleConfigs);
        assertEquals(2, exampleConfigs.size());
        assertThat(exampleConfigs).contains(exampleConfig1, exampleConfig2);
    }

    @Test
    void findPageOfExampleConfigs() {
        ExampleConfig exampleConfig = insertExampleConfig("exampleConfig1", ExampleConfig.Visibility.PRIVATE, LocalDate.now(), true,
                                                          insertExampleConfigType(ExampleConfigType.Type.T1, "type1"));
        insertExampleConfig("exampleConfig2", ExampleConfig.Visibility.PUBLIC, LocalDate.now(), false,
                            insertExampleConfigType(ExampleConfigType.Type.T2, "type2"));

        Page<ExampleConfig> exampleConfigsPage = exampleConfigRepository.findAll(PageRequest.of(0, 1));

        assertNotNull(exampleConfigsPage);
        assertNotNull(exampleConfigsPage.getContent());
        assertEquals(1, exampleConfigsPage.getContent().size());
        assertThat(exampleConfigsPage.getContent()).contains(exampleConfig);
    }

    @Test
    void noExampleConfigsFound() {
        List<ExampleConfig> exampleConfigs = exampleConfigRepository.findAll();

        assertTrue(exampleConfigs.isEmpty());
    }

    @Test
    void findExampleConfigById() {
        ExampleConfig exampleConfig = insertExampleConfig("exampleConfig1", ExampleConfig.Visibility.PRIVATE, LocalDate.now(), true,
                                                          insertExampleConfigType(ExampleConfigType.Type.T1, "type1"));

        Optional<ExampleConfig> foundExampleConfig = exampleConfigRepository.findById(exampleConfig.getId());

        assertTrue(foundExampleConfig.isPresent());
        assertEquals(exampleConfig, foundExampleConfig.get());
    }

    @Test
    void exampleConfigNotFound() {
        Optional<ExampleConfig> foundExampleConfig = exampleConfigRepository.findById(1L);

        assertTrue(foundExampleConfig.isEmpty());
    }

    @Test
    void findAllExampleConfigsByVisibility() {
        insertExampleConfig("exampleConfig1", ExampleConfig.Visibility.PRIVATE, LocalDate.now(), true,
                            insertExampleConfigType(ExampleConfigType.Type.T1, "type1"));
        ExampleConfig exampleConfig = insertExampleConfig("exampleConfig2", ExampleConfig.Visibility.PUBLIC, LocalDate.now(), false,
                                                          insertExampleConfigType(ExampleConfigType.Type.T2, "type2"));

        Page<ExampleConfig> exampleConfigs = exampleConfigRepository.findByVisibility(ExampleConfig.Visibility.PUBLIC, Pageable.unpaged());

        assertNotNull(exampleConfigs);
        assertNotNull(exampleConfigs.getContent());
        assertEquals(1, exampleConfigs.getContent().size());
        assertThat(exampleConfigs.getContent()).contains(exampleConfig);
    }

    @Test
    void findPageOfExampleConfigsByVisibility() {
        insertExampleConfig("exampleConfig1", ExampleConfig.Visibility.PRIVATE, LocalDate.now(), true,
                            insertExampleConfigType(ExampleConfigType.Type.T1, "type1"));
        insertExampleConfig("exampleConfig2", ExampleConfig.Visibility.PUBLIC, LocalDate.now(), false,
                            insertExampleConfigType(ExampleConfigType.Type.T2, "type2"));
        ExampleConfig exampleConfig = insertExampleConfig("exampleConfig3", ExampleConfig.Visibility.PUBLIC, LocalDate.now(), true,
                                                          insertExampleConfigType(ExampleConfigType.Type.T3, "type3"));

        Page<ExampleConfig> exampleConfigs = exampleConfigRepository.findByVisibility(ExampleConfig.Visibility.PUBLIC, PageRequest.of(1, 1));

        assertNotNull(exampleConfigs);
        assertNotNull(exampleConfigs.getContent());
        assertEquals(1, exampleConfigs.getContent().size());
        assertThat(exampleConfigs.getContent()).contains(exampleConfig);
    }

    @Test
    void createExampleConfig() {
        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setName("exampleConfig");
        exampleConfig.setVisibility(ExampleConfig.Visibility.PUBLIC);
        exampleConfig.setCreateDate(LocalDate.now());
        exampleConfig.setExampleConfigType(insertExampleConfigType(ExampleConfigType.Type.T1, "type1"));
        exampleConfig.setActive(true);

        ExampleConfig createdExampleConfig = exampleConfigRepository.save(exampleConfig);

        assertNotNull(createdExampleConfig);
        assertEquals(exampleConfig, createdExampleConfig);
    }

    @Test
    void updateExampleConfig() {
        ExampleConfigType exampleConfigType1 = insertExampleConfigType(ExampleConfigType.Type.T1, "type1");
        ExampleConfigType exampleConfigType2 = insertExampleConfigType(ExampleConfigType.Type.T2, "type2");

        ExampleConfig exampleConfig = insertExampleConfig("exampleConfig", ExampleConfig.Visibility.PRIVATE, LocalDate.now(), true, exampleConfigType1);
        String name = "exampleConfig1";
        exampleConfig.setName(name);
        LocalDate createDate = LocalDate.now();
        exampleConfig.setCreateDate(createDate);
        exampleConfig.setVisibility(ExampleConfig.Visibility.PUBLIC);
        exampleConfig.setActive(false);
        exampleConfig.setExampleConfigTypeValue(exampleConfigType2.getId().toString());

        ExampleConfig updatedExampleConfig = exampleConfigRepository.save(exampleConfig);

        assertNotNull(updatedExampleConfig);
        assertAll("ExampleConfig data",
                  () -> assertEquals(exampleConfig.getId(), updatedExampleConfig.getId()),
                  () -> assertEquals(name, updatedExampleConfig.getName()),
                  () -> assertEquals(createDate, updatedExampleConfig.getCreateDate()),
                  () -> assertEquals(ExampleConfig.Visibility.PUBLIC, updatedExampleConfig.getVisibility()),
                  () -> assertFalse(updatedExampleConfig.isActive()),
                  () -> assertEquals(exampleConfigType2.getId(), updatedExampleConfig.getExampleConfigType().getId())
        );
    }

    @Test
    void deleteExampleConfig() {
        ExampleConfig exampleConfig = insertExampleConfig("exampleConfig1", ExampleConfig.Visibility.PRIVATE, LocalDate.now(), true,
                                                          insertExampleConfigType(ExampleConfigType.Type.T1, "type1"));

        exampleConfigRepository.deleteById(exampleConfig.getId());
        Optional<ExampleConfig> noExampleConfig = exampleConfigRepository.findById(exampleConfig.getId());

        assertTrue(noExampleConfig.isEmpty());
    }

    private ExampleConfig insertExampleConfig(String name, ExampleConfig.Visibility visibility, LocalDate createDate, boolean active,
                                              ExampleConfigType exampleConfigType) {
        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setName(name);
        exampleConfig.setVisibility(visibility);
        exampleConfig.setCreateDate(createDate);
        exampleConfig.setExampleConfigType(exampleConfigType);
        exampleConfig.setActive(active);

        return exampleConfigRepository.save(exampleConfig);
    }

    private ExampleConfigType insertExampleConfigType(ExampleConfigType.Type type, String description) {
        ExampleConfigType exampleConfigType = new ExampleConfigType();
        exampleConfigType.setType(type);
        exampleConfigType.setDescription(description);

        return exampleConfigTypeRepository.save(exampleConfigType);
    }
}
