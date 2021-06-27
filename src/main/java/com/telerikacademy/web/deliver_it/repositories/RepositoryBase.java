package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.repositories.contracts.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public abstract class RepositoryBase<E> implements Repository<E> {
    private SessionFactory sessionFactory;

    public RepositoryBase(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public RepositoryBase(){}

    @Override
    public E create(E entity) {
        try (Session session = sessionFactory.openSession()) {
            session.save(entity);
            return getByName(entity);
        }
    }

    @Override
    public E update(E entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();

            return getByName(entity);
        }
    }

    @Override
    public E delete(int id) {
        return delete(getById(id));
    }

    @Override
    public E delete(E entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();

            return entity;
        }
    }

    @Override
    public E put(E entity) {
        try {
            return getByName(entity);
        } catch (EntityNotFoundException e) {
            return create(entity);
        }
    }

    @Override
    public abstract List<E> getAll();

    @Override
    public abstract E getById(int id);

    @Override
    public abstract E getByName(E entity);

    protected List<E> getAll(Class<E> classType, String className) {
        String queryString = String.format("from %s ", className);

        try (Session session = sessionFactory.openSession()) {
            Query<E> query = session.createQuery(
                    queryString, classType);

            return query.list();
        }
    }

    protected List<E> getBy(Query<E> query, String errMessage) {
        return getFrom(query, errMessage);
    }

    protected E getByName(Query<E> query, String errMessage) {
        return getFrom(query, errMessage).get(0);
    }

    protected E getById(Class<E> classType, int id, String className) {
        try (Session session = sessionFactory.openSession()) {
            E entity = session.get(classType, id);
            if (entity == null)
                throw new EntityNotFoundException(className, id);
            return entity;
        }
    }

    protected String getErrMessageBy(String type, String name) {
        return String.format("%s whit name %s not found", type, name);
    }

    protected String notFound(String entity) {
        return String.format("%s not found", entity);
    }

    private List<E> getFrom(Query<E> query, String errMessage) {
        List<E> result = query.list();
        if (result.size() == 0)
            throw new EntityNotFoundException(errMessage);
        return result;
    }
}
