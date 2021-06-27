package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.services.contracts.CityService;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;


@Controller
@RequestMapping("/cities")
public class CityMvcController {

    private final CityService service;

    @Autowired
    public CityMvcController(CityService service) {
        this.service = service;
    }

    @GetMapping(GET_BY_ID_URL)
    public String getById(@PathVariable int id, Model model) {
        try {
            City city = service.getById(id);
            model.addAttribute(CITY_ATTRIBUTE, city);
            return CITY_BY_ID_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        }
    }

    /*
    @GetMapping(GET_BY_NAME_URL)
    public String getByName(@RequestParam String name,
                            @RequestParam String country,
                            Model model) {
        try {
            //SecurityCredentials sc = SC_EMPLOYEE;
            City city = service.getByName(name, country);
            model.addAttribute(CITY_ATTRIBUTE, city);
            return CITY_BY_ID_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        }
    }
    */
}
