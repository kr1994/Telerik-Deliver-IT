package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.Address;
import com.telerikacademy.web.deliver_it.repositories.contracts.AddressRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Queries.ADDRESS_BY_NAME_MATCHES;
import static com.telerikacademy.web.deliver_it.utils.QuerySetters.setData;

@Repository
public class AddressRepositoryImpl extends RepositoryBase<Address> implements AddressRepository {

    public static final String CLASS_NAME = "Address";
    private final SessionFactory sessionFactory;

    @Autowired
    public AddressRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Address> getAll() {
        return super.getAll(Address.class, CLASS_NAME);
    }

    @Override
    public Address getById(int id) {
        return super.getById(Address.class, id, CLASS_NAME);
    }

    @Override
    public Address getByName(Address address) {
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery(
                    " from Address as a " +
                            " where " + ADDRESS_BY_NAME_MATCHES, Address.class);
            setData(address, query);

            return getByName(query, notFound(CLASS_NAME));
        }
    }

    @Override
    public Address getByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery(
                    " select cu.delivery " +
                            " from Customer as cu " +
                            " where cu.userId = :id ", Address.class);
            query.setParameter("id", userId);

            return getByName(query, notFound(CLASS_NAME));
        }
    }
}
