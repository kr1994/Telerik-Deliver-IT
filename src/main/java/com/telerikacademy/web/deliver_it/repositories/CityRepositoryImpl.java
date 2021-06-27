package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.repositories.contracts.CityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Queries.CITY_NAME;
import static com.telerikacademy.web.deliver_it.utils.Queries.COUNTRY_NAME;
import static com.telerikacademy.web.deliver_it.utils.QuerySetters.setData;

@Repository
public class CityRepositoryImpl extends RepositoryBase<City> implements CityRepository {

    private static final String CLASS_NAME = "City";
    private final SessionFactory sessionFactory;

    @Autowired
    public CityRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<City> getAll() {
        return super.getAll(City.class, CLASS_NAME);
    }

    @Override
    public City getById(int id) {
        return super.getById(City.class, id, CLASS_NAME);
    }

    @Override
    public City getByName(City city) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery(
                    " from City as ci " +
                            " where ci.cityName = :" + CITY_NAME +
                            " and ci.country.countryName = :" + COUNTRY_NAME, City.class);
            setData(city, query);
            String errMessage = String.format("City whit name %s in country %s not found",
                    city.getCityName(), city.getCountryName());
            return getByName(query, errMessage);
        }
    }
}
