package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.City;
import com.telerikacademy.web.deliver_it.models.Country;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.Warehouse;
import com.telerikacademy.web.deliver_it.models.dto.AddressDto;
import com.telerikacademy.web.deliver_it.services.contracts.CityService;
import com.telerikacademy.web.deliver_it.services.contracts.CountryService;
import com.telerikacademy.web.deliver_it.services.contracts.WarehouseService;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoMappers;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;
import static com.telerikacademy.web.deliver_it.controllers.mvc.MvcHelpers.*;
import static com.telerikacademy.web.deliver_it.utils.Builders.buildDto;

@Controller
@RequestMapping(WAREHOUSES_URL)
public class WarehouseMvcController {

    private final WarehouseService service;
    private final CountryService countries;
    private final CityService cities;
    private final DtoMappers mapper;
    private final Security security;

    @Autowired
    public WarehouseMvcController(WarehouseService service, DtoMappers mapper, CityService cities,
                                  CountryService countries, Security security) {
        this.countries = countries;
        this.security = security;
        this.service = service;
        this.mapper = mapper;
        this.cities = cities;
    }

    @GetMapping
    public String showAllWarehouses(Model model) {
        model.addAttribute(FILTER_BY_COUNTRY_ATTRIBUTE, new Country());
        return WAREHOUSES_VIEW;
    }

    @PostMapping
    public String showByCountry(@ModelAttribute(FILTER_BY_COUNTRY_ATTRIBUTE) Country country, Model model) {
        try {
            setWarehousesByCountryTo(model, country);
            return WAREHOUSES_VIEW;
        } catch (EntityNotFoundException e) {
            return showAllWarehouses(model);
        }
    }

    @GetMapping(GET_BY_ID_URL)
    public String showSingleWarehouse(@PathVariable int id, Model model) {
        try {
            setWarehouseTo(model, id);
            return WAREHOUSE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        }
    }

    @GetMapping(CREATE_URL)
    public String showCreationPage(Model model, HttpSession session) {
        try {
            validateEmployee(session, security);
            model.addAttribute(ADDRESS_ATTRIBUTE, new AddressDto());
            return WAREHOUSE_CREATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(CREATE_URL)
    public String create(@Valid @ModelAttribute(ADDRESS_ATTRIBUTE) AddressDto dto,
                         BindingResult errors, HttpSession session, Model model) {
        if (errors.hasErrors())
            return WAREHOUSE_CREATE_VIEW;

        try {
            Warehouse result = create(dto, session);
            return viewById(result);

        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("street1", "address.exists", e.getMessage());
            return WAREHOUSE_CREATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(UPDATE_URL)
    public String showUpdatePage(@PathVariable int id, HttpSession session, Model model) {
        try {
            validateEmployee(session, security);
            setAddressDtoTo(model, id);

            return WAREHOUSE_UPDATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(UPDATE_URL)
    public String update(@Valid @ModelAttribute(ADDRESS_ATTRIBUTE) AddressDto dto, @PathVariable int id,
                         BindingResult errors, HttpSession session, Model model) {
        if (errors.hasErrors())
            return WAREHOUSE_UPDATE_VIEW;
        try {
            Warehouse result = update(dto, id, session);
            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("street1", "address.exists", e.getMessage());
            return WAREHOUSE_UPDATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(DELETE_URL)
    public String showDeletePage(@PathVariable int id, HttpSession session, Model model) {
        try {
            delete(id, session, model);
            return WAREHOUSE_DELETE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @ModelAttribute(CITIES_ATTRIBUTE)
    public List<City> populateCities() {
        return cities.getAll();
    }

    @ModelAttribute(COUNTRIES_ATTRIBUTE)
    public List<Country> populateCountries() {
        return countries.getAll();
    }

    @ModelAttribute(WAREHOUSES_ATTRIBUTE)
    public List<Warehouse> populateWarehouses() {
        return service.getAll();
    }

    @ModelAttribute(COUNTRIES_WITH_WAREHOUSE_ATTRIBUTE)
    public List<Country> populateCountriesWhitWarehouse() {
        return countries.getCountriesForWarehouses();
    }

    private String viewById(Warehouse result) {
        return String.format("redirect:/warehouses/%d", result.getId());
    }

    private void setAddressDtoTo(Model model, int warehouseId) {
        Warehouse stock = service.getById(warehouseId);
        AddressDto dto = buildDto(stock.getAddress());
        model.addAttribute(ADDRESS_ATTRIBUTE, dto);
    }

    private Warehouse create(AddressDto dto, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        Warehouse input = mapper.warehouseFromDto(dto);
        return service.create(input, sc);
    }

    private Warehouse update(AddressDto dto, int id, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        Warehouse input = mapper.warehouseFromDto(id, dto);
        return service.update(input, sc);
    }

    private void delete(int id, HttpSession session, Model model) {
        SecurityCredentials sc = getCredential(session);
        Warehouse deleted = service.delete(id, sc);
        model.addAttribute(WAREHOUSE_ATTRIBUTE, deleted);
    }

    private void setWarehouseTo(Model model, int warehouseId) {
        Warehouse warehouse = service.getById(warehouseId);
        model.addAttribute(WAREHOUSE_ATTRIBUTE, warehouse);
    }

    private void setWarehousesByCountryTo(Model model, Country country) {
        List<Warehouse> warehousesByCountry = service.getByCountry(country.getCountryName());
        model.addAttribute(WAREHOUSES_ATTRIBUTE, warehousesByCountry);
    }

}
