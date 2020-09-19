package org.mi.adminui.data.feature.exampleconfig.repository;

import org.mi.adminui.data.core.repository.CrudJpaRepository;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleConfigRepository extends CrudJpaRepository<ExampleConfig, Long> {

    Page<ExampleConfig> findByVisibility(Visibility visibility, Pageable pageable);
}
