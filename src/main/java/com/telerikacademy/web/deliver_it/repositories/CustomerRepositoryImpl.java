package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.repositories.contracts.CustomerRepository;
import com.telerikacademy.web.deliver_it.utils.QuerySetters;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.deliver_it.utils.Queries.*;
import static com.telerikacademy.web.deliver_it.utils.QuerySetters.*;


@Repository
public class CustomerRepositoryImpl extends RepositoryBase<Customer> implements CustomerRepository {

    private static final String CLASS_NAME = "Customer";

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomerRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Customer> getAll() {
        return super.getAll(Customer.class, CLASS_NAME);
    }

    @Override
    public List<Customer> get_And_Logic(Optional<String> firstName, Optional<String> lastName,
                                        Optional<String> email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(
                    " from Customer as cu " +
                            " where " + ALL_USER_LIKE_AND, Customer.class);
            setUserLike(firstName, lastName, email, query);

            return getBy(query, notFound("Customers"));
        }
    }

    @Override
    public List<Customer> get_Or_Logic(Optional<String> firstName, Optional<String> lastName,
                                       Optional<String> email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(
                    " from Customer as cu " +
                            " where " + ALL_USER_LIKE_OR, Customer.class);
            setUserLike(firstName, lastName, email, query);

            return getBy(query, notFound("Customers"));
        }
    }

    @Override
    public Customer getById(int id) {
        return getById(Customer.class, id, CLASS_NAME);
    }

    @Override
    public Customer getByName(Customer customer) {
        Address address = customer.getDelivery();

        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(
                    " select u " +
                            " from Customer as u " +
                            " join u.delivery as a " +
                            " where " + ADDRESS_BY_NAME_MATCHES +
                            " and " + USER_BY_NAME_MATCHES, Customer.class);
            QuerySetters.setData(address, query);
            setUserData(customer, query);

            return getByName(query, notFound(CLASS_NAME));
        }
    }

    @Override
    public int numberOfCustomers() {
        return getAll().size();
    }
}