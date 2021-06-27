package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.repositories.contracts.WarehouseRepository;
import com.telerikacademy.web.deliver_it.utils.QuerySetters;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Queries.*;
import static com.telerikacademy.web.deliver_it.utils.QuerySetters.*;

@Repository
public class WarehouseRepositoryImpl extends RepositoryBase<Warehouse> implements WarehouseRepository {

    public static final String WAREHOUSE = "Warehouse";
    private final SessionFactory sessionFactory;

    @Autowired
    public WarehouseRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Warehouse> getAll() {
        return super.getAll(Warehouse.class, WAREHOUSE);
    }

    @Override
    public List<Warehouse> getBy(Country country) {
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery(
                    " select w " +
                            " from Warehouse as w " +
                            " join w.address as a " +
                            " where " + COUNTRY_INFO_MATCHES, Warehouse.class);
            setIds(country, query);
            setData(country, query);

            String errMessage = String.format("Warehouse not found in Country %s whit id %d.",
                    country.getCountryName(), country.getId());
            return getBy(query, errMessage);
        }
    }

    @Override
    public List<Warehouse> getBy(City city) {
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery(
                    " select w " +
                            " from Warehouse as w " +
                            " join w.address as a " +
                            " where " + CITY_INFO_MATCHES +
                            " and " + COUNTRY_INFO_MATCHES, Warehouse.class);
            setIds(city, query);
            setData(city, query);

            String errMessage = String.format("Warehouse not found in City %s id: %d, and Country %s id: %d",
                    city.getCityName(), city.getId(), city.getCountryName(), city.getCountry().getId());

            return getBy(query, errMessage);
        }
    }

    @Override
    public Warehouse getById(int id) {
        return super.getById(Warehouse.class, id, WAREHOUSE);
    }

    @Override
    public Warehouse getByName(Warehouse warehouse) {
        Address address = warehouse.getAddress();
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery(
                    " select w " +
                            " from Warehouse as w " +
                            " join w.address as a " +
                            " where " + ADDRESS_BY_NAME_MATCHES, Warehouse.class);
            setData(address, query);

            return getByName(query, notFound(WAREHOUSE));
        }
    }
}

