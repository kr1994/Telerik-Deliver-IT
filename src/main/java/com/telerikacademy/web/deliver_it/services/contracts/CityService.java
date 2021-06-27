package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.services.contracts.sub.GetAll;
import com.telerikacademy.web.deliver_it.services.contracts.sub.GetById;

public interface CityService extends GetById<City>,
        GetAll<City> {

    City getByName(String city, String country);
}
