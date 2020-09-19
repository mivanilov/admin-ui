package org.mi.adminui.data.feature.exampleconfig.repository;

import org.junit.jupiter.api.Test;
import org.mi.adminui.Application;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {Application.class})
@ActiveProfiles("test")
@Transactional
class ExampleConfigTypeRepositoryIT {

    @Autowired
    private ExampleConfigTypeRepository repository;

    @Test
    void findAllExampleConfigTypes() {
        ExampleConfigType exampleConfigType1 = insertExampleConfigType(1L, "type1", ExampleConfigType.Type.T1);
        ExampleConfigType exampleConfigType2 = insertExampleConfigType(2L, "type2", ExampleConfigType.Type.T2);

        List<ExampleConfigType> exampleConfigTypes = repository.findAll();

        assertNotNull(exampleConfigTypes);
        assertEquals(2, exampleConfigTypes.size());
        assertThat(exampleConfigTypes).contains(exampleConfigType1, exampleConfigType2);
    }

    private ExampleConfigType insertExampleConfigType(long id, String description, ExampleConfigType.Type type) {
        ExampleConfigType exampleConfigType = new ExampleConfigType();
        exampleConfigType.setId(id);
        exampleConfigType.setDescription(description);
        exampleConfigType.setType(type);

        return repository.save(exampleConfigType);
    }
}
