package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;
import com.telerikacademy.web.deliver_it.repositories.contracts.Repository;
import com.telerikacademy.web.deliver_it.services.contracts.Service;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;

public abstract class ServiceBase<E extends IdentifyAble> implements Service<E> {

    private final Repository<E> repo;
    private final Security security;

    protected ServiceBase(Repository<E> repo, Security security) {
        this.security = security;
        this.repo = repo;
    }

    @Override//employee
    public E getById(int id, SecurityCredentials sc) {
        validateEmployee(sc);
        return repo.getById(id);
    }

    @Override//employee
    public E create(E entity, SecurityCredentials sc) {
        validateEmployee(sc);
        validateUnique(entity);

        return repo.create(entity);
    }

    @Override//employee
    public E update(E entity, SecurityCredentials sc) {
        validateEmployee(sc);
        validateExists(entity);

        return repo.update(entity);
    }

    @Override//employee
    public E delete(int id, SecurityCredentials sc) {
        validateEmployee(sc);
        return repo.delete(id);
    }

    protected boolean isEmployee(SecurityCredentials sc) {
        return security.isEmployee(sc);
    }

    protected boolean isCustomer(SecurityCredentials sc) {
        return security.isCustomer(sc);
    }

    protected void validateEmployee(SecurityCredentials sc) {
        security.validateEmployee(sc);
    }

    protected void validateCustomer(int id, SecurityCredentials sc) {
        security.validateCustomer(id, sc);
    }

    protected void validateExists(E entity) {
        repo.getById(entity.getId());
    }

    protected void validateUnique(E entity) {
        try {
            repo.getByName(entity);
            throw new DuplicateEntityException("It already exists");
        } catch (EntityNotFoundException ignored) {
        }
    }

}
