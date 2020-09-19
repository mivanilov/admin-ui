package org.mi.adminui.data.core.service;

import org.mi.adminui.data.core.model.CrudEntity;

import java.util.List;
import java.util.Optional;

public interface CrudDataService<E extends CrudEntity<ID>, ID> {

    List<E> findAll();

    Optional<E> find(ID id);

    E create(E entity);

    E update(E entity);

    void delete(ID id);
}