package com.telerikacademy.web.deliver_it.controllers.rest;

import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.AddressDto;
import com.telerikacademy.web.deliver_it.services.contracts.WarehouseService;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/deliver_it/warehouses")
public class WarehouseController {

    private final WarehouseService service;
    private final DtoMappers mapper;

    @Autowired
    public WarehouseController(WarehouseService service, DtoMappers mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Warehouse> getAll() {
        return service.getAll();
    }

    @GetMapping("/country")
    public List<Warehouse> getByCountry(@NotBlank @RequestParam String name) {
        try {
            return service.getByCountry(name);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Warehouse getById(@Positive @PathVariable int id) {
        return ControllerHelper.getById(id, service);
    }

    @PostMapping
    public Warehouse create(@Valid @RequestBody AddressDto dto,
                            @RequestHeader HttpHeaders header) {
        Warehouse warehouse = fromDto(dto);
        return ControllerHelper.create(warehouse, header, service);
    }


    @PutMapping("/{id}")
    public Warehouse update(@Positive @PathVariable int id,
                            @Valid @RequestBody AddressDto dto,
                            @RequestHeader HttpHeaders header) {
        Warehouse warehouse = fromDto(id, dto);
        return ControllerHelper.update(warehouse, header, service);
    }

    @DeleteMapping("/{id}")
    public Warehouse delete(@Positive @PathVariable int id,
                            @RequestHeader HttpHeaders header) {
        return ControllerHelper.delete(id, header, service);
    }

    private Warehouse fromDto(AddressDto addressDto) {
        try {
            return mapper.warehouseFromDto(addressDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private Warehouse fromDto(int id, AddressDto addressDto) {
        try {
            return mapper.warehouseFromDto(id, addressDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
