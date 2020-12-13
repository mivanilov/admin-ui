package org.mi.adminui.data.core.service;

import org.mi.adminui.exception.RecordCreateException;
import org.mi.adminui.exception.RecordNotFoundException;
import org.mi.adminui.data.core.model.CrudEntity;
import org.mi.adminui.data.core.repository.CrudJpaRepository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudDataService<E extends CrudEntity<ID>, ID> implements CrudDataService<E, ID> {

    protected static final String RECORD_TO_INSERT_ALREADY_EXISTS = "Record to insert already exists";
    protected static final String RECORD_TO_UPDATE_NOT_FOUND = "Record to update not found";
    protected static final String RECORD_TO_DELETE_NOT_FOUND = "Record to delete not found";

    protected final CrudJpaRepository<E, ID> crudJpaRepository;

    public AbstractCrudDataService(CrudJpaRepository<E, ID> crudJpaRepository) {
        this.crudJpaRepository = crudJpaRepository;
    }

    @Override
    public List<E> findAll() {
        return crudJpaRepository.findAll();
    }

    @Override
    public Optional<E> find(ID id) {
        return crudJpaRepository.findById(id);
    }

    @Override
    public E create(E entity) {
        if (entity.getId() != null && crudJpaRepository.existsById(entity.getId())) {
            throw new RecordCreateException(RECORD_TO_INSERT_ALREADY_EXISTS);
        }

        return crudJpaRepository.save(entity);
    }

    @Override
    public E update(E entity) {
        if (!crudJpaRepository.existsById(entity.getId())) {
            throw new RecordNotFoundException(RECORD_TO_UPDATE_NOT_FOUND);
        }

        return crudJpaRepository.save(entity);
    }

    @Override
    public void delete(ID id) {
        try {
            crudJpaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(RECORD_TO_DELETE_NOT_FOUND, e);
        }
    }
}
