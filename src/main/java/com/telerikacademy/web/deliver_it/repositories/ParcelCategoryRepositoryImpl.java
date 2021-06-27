package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.ParcelCategory;
import com.telerikacademy.web.deliver_it.repositories.contracts.ParcelCategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParcelCategoryRepositoryImpl
        extends RepositoryBase<ParcelCategory>
        implements ParcelCategoryRepository {

    public static final String CLASS_NAME = "ParcelCategory";
    private final SessionFactory sessionFactory;

    @Autowired
    public ParcelCategoryRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ParcelCategory> getAll() {
        return super.getAll(ParcelCategory.class, CLASS_NAME);
    }

    @Override
    public ParcelCategory getById(int id) {
        return super.getById(ParcelCategory.class, id, CLASS_NAME);
    }

    @Override
    public ParcelCategory getByName(ParcelCategory parcelCategory) {
        try (Session session = sessionFactory.openSession()) {
            Query<ParcelCategory> query = session.createQuery(
                    " from ParcelCategory " +
                            " where parcelCategory = :pc ", ParcelCategory.class);
            query.setParameter("pc",parcelCategory);

            return getByName(query, getErrMessageBy(CLASS_NAME, parcelCategory.getParcelCategory()));
        }
    }
}
