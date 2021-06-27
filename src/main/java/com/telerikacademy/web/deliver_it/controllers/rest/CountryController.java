package com.telerikacademy.web.deliver_it.controllers.rest;

import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/deliver_it/countries")
public class CountryController {

    private final CountryService service;

    @Autowired
    public CountryController(CountryService countryService) {
        this.service = countryService;
    }

    @GetMapping
    public List<Country> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Country getById(@Positive @PathVariable int id) {
        return ControllerHelper.getById(id, service);
    }

    @GetMapping("/search")
    public Country getByName(@NotBlank @RequestParam() String name) {
        try {
            return service.getByName(name);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
