package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.WeightUnit;
import com.telerikacademy.web.deliver_it.repositories.contracts.WeightUnitRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class WeightUnitRepositoryImpl
        extends RepositoryBase<WeightUnit> implements WeightUnitRepository {

    public static final String CLASS_NAME = "WeightUnit";
    private final SessionFactory sessionFactory;

    @Autowired
    public WeightUnitRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<WeightUnit> getAll() {
        return super.getAll(WeightUnit.class, CLASS_NAME);
    }

    @Override
    public WeightUnit getById(int id) {
        return super.getById(WeightUnit.class, id, CLASS_NAME);
    }

    @Override
    public WeightUnit getByName(WeightUnit wu) {
        try (Session session = sessionFactory.openSession()) {
            Query<WeightUnit> query = session.createQuery(
                    " from WeightUnit " +
                            " where units = :wu ", WeightUnit.class);
            query.setParameter("wu", wu);
            return getByName(query, getErrMessageBy("Weight unit", wu.getUnits()));
        }
    }
}
