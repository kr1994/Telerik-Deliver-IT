package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.CustomerDto;
import com.telerikacademy.web.deliver_it.models.dto.CustomerSearchDto;
import com.telerikacademy.web.deliver_it.services.contracts.CustomerService;
import com.telerikacademy.web.deliver_it.services.contracts.WarehouseService;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.CustomerMapper;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;
import static com.telerikacademy.web.deliver_it.controllers.mvc.MvcHelpers.getCredential;
import static com.telerikacademy.web.deliver_it.controllers.mvc.MvcHelpers.validateEmployee;
import static com.telerikacademy.web.deliver_it.utils.Builders.buildDto;

@Controller
@RequestMapping("/customers")
public class CustomerMvcController {

    private final WarehouseService warehouses;
    private final CustomerService service;
    private final CustomerMapper mapper;
    private final Security security;

    @Autowired
    public CustomerMvcController(CustomerService service, WarehouseService warehouses,
                                 CustomerMapper mapper, Security security) {
        this.warehouses = warehouses;
        this.security = security;
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(GET_BY_ID_URL)
    public String getById(@PathVariable int id, HttpSession session, Model model) {
        try {
            SecurityCredentials sc = getCredential(session);
            Customer customer = service.getById(id, sc);
            model.addAttribute(CUSTOMER_ATTRIBUTE, customer);
            return CUSTOMER_BY_ID_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }


    @GetMapping("/search")
    public String showSearchPage(HttpSession session, Model model) {
        try {
            validateEmployee(session, security);
            model.addAttribute(CUSTOMER_SEARCH_DTO, new CustomerSearchDto());
            return "customer-search";
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping("/search")
    public String search(@ModelAttribute(CUSTOMER_SEARCH_DTO) CustomerSearchDto dto,
                         HttpSession session, Model model) {
        try {
            List<Customer> result = search(dto, session);
            model.addAttribute(CUSTOMERS_ATTRIBUTE, result);
            return CUSTOMERS_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }catch (EntityNotFoundException e){
            return NOT_FOUND_VIEW;
        }
    }

    private List<Customer> search(CustomerSearchDto dto, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        Optional<String> firstName = getOptional(dto.getFirstName());
        Optional<String> lastName = getOptional(dto.getLastName());
        Optional<String> email = getOptional(dto.getEmail());
        Optional<String> any = getOptional(dto.getAny());
        return service.getAll(firstName, lastName, email, any, sc);
    }


    @GetMapping
    public String showAllCustomers(Model model, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        model.addAttribute(CUSTOMERS_ATTRIBUTE, service.getAll(sc));
        return CUSTOMERS_VIEW;
    }

    @PostMapping
    public String findAllCustomers(Model model, HttpSession session) {
        try {
            SecurityCredentials sc = getCredential(session);
            ArrayList<Customer> customers = (ArrayList<Customer>) service.getAll(sc);
            model.addAttribute(CUSTOMERS_ATTRIBUTE, customers);
            return CUSTOMERS_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        }
    }

    @GetMapping(CREATE_URL)
    public String showCreationPage(Model model) {
        model.addAttribute(CUSTOMER_ATTRIBUTE, new CustomerDto());
        return CUSTOMER_CREATE_VIEW;
    }

    @PostMapping(CREATE_URL)
    public String create(@Valid @ModelAttribute(CUSTOMER_ATTRIBUTE) CustomerDto dto,
                         BindingResult errors, Model model) {
        try {
            if (errors.hasErrors())
                return CUSTOMER_CREATE_VIEW;

            Customer input = mapper.fromDto(dto);
            Customer result = service.create(input);

            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("firstName", "customer.exists", e.getMessage());
            return CUSTOMER_CREATE_VIEW;
        }
    }

    @GetMapping(UPDATE_URL)
    public String showUpdatePage(@PathVariable int id, Model model, HttpSession session) {
        try {
            SecurityCredentials sc = getCredential(session);

            Customer customer = service.getById(id, sc);
            CustomerDto dto = buildDto(customer);
            model.addAttribute(CUSTOMER_ATTRIBUTE, dto);

            return CUSTOMER_UPDATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(UPDATE_URL)
    public String update(@Valid @ModelAttribute(CUSTOMER_ATTRIBUTE) CustomerDto dto,
                         @PathVariable int id, HttpSession session,
                         BindingResult errors, Model model) {
        if (errors.hasErrors())
            return CUSTOMER_UPDATE_VIEW;

        try {
            SecurityCredentials sc = getCredential(session);

            Customer input = mapper.fromDto(id, dto);
            Customer result = service.update(input, sc);

            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("firstName", "customer.exists", e.getMessage());
            return CUSTOMER_UPDATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(DELETE_URL)
    public String showDeletePage(@PathVariable int id, Model model, HttpSession session) {
        try {
            SecurityCredentials sc = getCredential(session);
            Customer deleted = service.delete(id, sc);

            model.addAttribute(CUSTOMER_ATTRIBUTE, deleted);
            return CUSTOMER_DELETE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @ModelAttribute(ADDRESSES_ATTRIBUTE)
    public List<Address> populateAddresses() {
        return warehouses.getAll().stream()
                .map(Warehouse::getAddress)
                .collect(Collectors.toList());
    }

    private String viewById(Customer result) {
        return String.format("redirect:/customers/%d", result.getId());
    }

    private Optional<String> getOptional(String string) {
        return string == null || string.isBlank() ?
                Optional.empty() :
                Optional.of(string);
    }
}
