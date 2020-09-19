package org.mi.adminui.data.core.repository;

import org.mi.adminui.data.core.model.CrudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudJpaRepository<E extends CrudEntity<ID>, ID> extends JpaRepository<E, ID> {
}
