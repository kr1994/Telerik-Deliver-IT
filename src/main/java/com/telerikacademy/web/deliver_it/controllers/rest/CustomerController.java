package com.telerikacademy.web.deliver_it.controllers.rest;

import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.CustomerMapper;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.CustomerDto;
import com.telerikacademy.web.deliver_it.services.contracts.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.buildCredential;

@RestController
@RequestMapping("/deliver_it/customers")
public class CustomerController {
    private final CustomerService service;

    private final CustomerMapper mapper;

    @Autowired
    public CustomerController(CustomerService service, CustomerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/numberOfUsers")
    public int getNumberOfUsers() {
        return service.numberOfCustomers();
    }

    @GetMapping("/search")
    public List<Customer> getAll(@RequestParam(required = false) Optional<String> firstName,
                                 @RequestParam(required = false) Optional<String> lastName,
                                 @RequestParam(required = false) Optional<String> email,
                                 @RequestParam(required = false) Optional<String> any,
                                 @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.getAll(firstName, lastName, email, any, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public Customer create(@Valid @RequestBody CustomerDto dto) {
        Customer customer = fromDto(dto);
        return ControllerHelper.create(customer, service);
    }

    @PutMapping("/{id}")
    public Customer update(@Valid @RequestBody CustomerDto dto,
                           @Positive @PathVariable int id,
                           @RequestHeader HttpHeaders header) {
        Customer customer = fromDto(id, dto);
        return ControllerHelper.update(customer, header, service);

    }

    @DeleteMapping("/{id}")
    public Customer delete(@Positive @PathVariable int id,
                           @RequestHeader HttpHeaders header) {
        return ControllerHelper.delete(id, header, service);
    }

    @GetMapping("/{id}")
    public Customer getById(@Positive @PathVariable int id,
                            @RequestHeader HttpHeaders header) {
        return ControllerHelper.getById(id, header, service);
    }

    private SecurityCredentials getCredential(HttpHeaders header) {
        return buildCredential(header);
    }

    private Customer fromDto(int id, CustomerDto customerDto) {
        try {
            return mapper.fromDto(id, customerDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private Customer fromDto(CustomerDto customerDto) {
        try {
            return mapper.fromDto(customerDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
