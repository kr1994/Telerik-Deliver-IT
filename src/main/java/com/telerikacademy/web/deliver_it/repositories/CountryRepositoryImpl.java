package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.repositories.contracts.CountryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Queries.COUNTRY_NAME;
import static com.telerikacademy.web.deliver_it.utils.QuerySetters.setData;

@Repository
public class CountryRepositoryImpl extends RepositoryBase<Country> implements CountryRepository {

    public static final String CLASS_NAME = "Country";
    private final SessionFactory sessionFactory;

    @Autowired
    public CountryRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> getAll() {
        return super.getAll(Country.class, CLASS_NAME);
    }

    @Override
    public List<Country> getCountriesForWarehouses(){
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery(
                    "select co" +
                            " from Warehouse as w " +
                            " join w.address as a " +
                            " join a.city as ci " +
                            " join ci.country as co" +
                            " group by co.countryName ", Country.class);
            return getBy(query, notFound("Countries"));
        }
    }

    @Override
    public Country getById(int id) {
        return super.getById(Country.class, id, CLASS_NAME);
    }

    @Override
    public Country getByName(Country country) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery(
                    " from Country " +
                            " where countryName = :" + COUNTRY_NAME, Country.class);
            setData(country, query);

            return getByName(query, getErrMessageBy(CLASS_NAME, country.getCountryName()));
        }
    }
}
