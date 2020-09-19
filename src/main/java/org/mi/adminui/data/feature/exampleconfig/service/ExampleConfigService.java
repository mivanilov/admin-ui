package org.mi.adminui.data.feature.exampleconfig.service;

import org.mi.adminui.data.core.service.AbstractCrudDataService;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig.Visibility;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.mi.adminui.data.feature.exampleconfig.repository.ExampleConfigRepository;
import org.mi.adminui.data.feature.exampleconfig.repository.ExampleConfigTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExampleConfigService extends AbstractCrudDataService<ExampleConfig, Long> {

    private final ExampleConfigTypeRepository exampleConfigTypeRepository;

    public ExampleConfigService(ExampleConfigRepository exampleConfigRepository,
                                ExampleConfigTypeRepository exampleConfigTypeRepository) {
        super(exampleConfigRepository);
        this.exampleConfigTypeRepository = exampleConfigTypeRepository;
    }

    public Page<ExampleConfig> findPageOfExampleConfigs(int pageNumber, int pageSize) {
        return crudJpaRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public List<ExampleConfig> findAllExampleConfigsByVisibility(Visibility visibility) {
        return ((ExampleConfigRepository) crudJpaRepository).findByVisibility(visibility, Pageable.unpaged()).getContent();
    }

    public Page<ExampleConfig> findPageOfExampleConfigsByVisibility(Visibility visibility, int pageNumber, int pageSize) {
        return ((ExampleConfigRepository) crudJpaRepository).findByVisibility(visibility, PageRequest.of(pageNumber, pageSize));
    }

    public List<ExampleConfigType> findAllExampleConfigTypes() {
        return exampleConfigTypeRepository.findAll();
    }
}
