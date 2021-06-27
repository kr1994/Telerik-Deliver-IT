package com.telerikacademy.web.deliver_it.controllers.rest;

import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/deliver_it/cities")
public class CityController {

    private final CityService service;

    @Autowired
    public CityController(CityService cityService) {
        this.service = cityService;
    }

    @GetMapping
    public List<City> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public City getById(@Positive @PathVariable int id) {
        return ControllerHelper.getById(id, service);
    }

    @GetMapping("/search")
    public City getByName(@NotBlank @RequestParam() String city,
                          @NotBlank @RequestParam() String country) {
        try {
            return service.getByName(city, country);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
