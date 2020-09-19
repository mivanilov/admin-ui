package org.mi.adminui.data.feature.exampleconfig.repository;

import org.mi.adminui.data.core.repository.CrudJpaRepository;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleConfigTypeRepository extends CrudJpaRepository<ExampleConfigType, Long> {
}
