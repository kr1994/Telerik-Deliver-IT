package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.services.contracts.CountryService;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;

@Controller
@RequestMapping("/countries")
public class CountryMvcController {

    private final CountryService service;

    @Autowired
    public CountryMvcController(CountryService service) {
        this.service = service;
    }

    @GetMapping(GET_BY_ID_URL)
    public String getById(@PathVariable int id, Model model) {
        try {
            Country country = service.getById(id);
            model.addAttribute(COUNTRY_ATTRIBUTE, country);
            return COUNTRY_BY_ID_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        }
    }

    @GetMapping(GET_BY_NAME_URL)
    public String getByName(@RequestParam String name, Model model) {
        try {
            Country country = service.getByName(name);
            model.addAttribute(COUNTRY_ATTRIBUTE, country);
            return COUNTRY_BY_ID_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        }
    }
}
