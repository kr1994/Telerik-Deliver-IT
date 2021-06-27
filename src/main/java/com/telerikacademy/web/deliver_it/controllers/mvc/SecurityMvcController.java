package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.CustomerDto;
import com.telerikacademy.web.deliver_it.services.contracts.CustomerService;
import com.telerikacademy.web.deliver_it.services.contracts.EmployeeService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;
import static com.telerikacademy.web.deliver_it.controllers.mvc.MvcHelpers.getCredential;
import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.buildCredential;

@Controller
@RequestMapping("/")
public class SecurityMvcController {

    private final WarehouseService warehouses;
    private final CustomerService customers;
    private final EmployeeService employees;
    private final CustomerMapper mapper;
    private final Security security;

    @Autowired
    public SecurityMvcController(CustomerService customers, EmployeeService employees, WarehouseService warehouses,
                                 Security security, CustomerMapper mapper) {
        this.customers = customers;
        this.employees = employees;
        this.warehouses = warehouses;
        this.security = security;
        this.mapper = mapper;
    }

    @GetMapping
    public String getWelcomePage(Model model) {
        model.addAttribute(NUMBER_OF_CUSTOMERS, customers.numberOfCustomers());
        return "index";
        //return "welcome-index";
    }

    @GetMapping(LOGIN_URL)
    public String showLoginPage(Model model, HttpSession session) {
        setCredentialsTo(model, session);
        return LOGIN_VIEW;
    }


    @PostMapping(LOGIN_URL)
    public String login(@Valid @ModelAttribute(CREDENTIALS) SecurityCredentials sc,
                        BindingResult errors, HttpSession session) {
        if (errors.hasErrors())
            return LOGIN_VIEW;
        try {
            login(sc, session);
            return INDEX_VIEW;
        } catch (EntityNotFoundException e) {
            errors.rejectValue("name", "login.failed", e.getMessage());
            return LOGIN_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(CREATE_URL)
    public String showRegisterPage(Model model) {
        model.addAttribute(CUSTOMER_ATTRIBUTE, new CustomerDto());
        return CUSTOMER_CREATE_VIEW;
    }

    @PostMapping(CREATE_URL)
    public String create(@Valid @ModelAttribute(CUSTOMER_ATTRIBUTE) CustomerDto dto,
                         BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors())
            return CUSTOMER_CREATE_VIEW;
        try {
            Customer result = create(dto);
            setCredentialsTo(session, result);

            return "redirect:/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("firstName", "customer.exists", e.getMessage());
            return CUSTOMER_CREATE_VIEW;
        }
    }

    @GetMapping(LOGOUT_URL)
    public String logout(HttpSession session) {
        session.removeAttribute(CREDENTIALS);
        session.removeAttribute(ROLE);
        session.removeAttribute(LAST_NAME);

        return "redirect:/";
    }

    @ModelAttribute(IS_LOGGED)
    public boolean setIsLogged(HttpSession session) {
        Object o = session.getAttribute(CREDENTIALS);
        return o != null &&
                !o.equals(new SecurityCredentials());
    }

    @ModelAttribute(ADDRESSES_ATTRIBUTE)
    public List<Address> populateAddresses() {
        return warehouses.getAll().stream()
                .map(Warehouse::getAddress)
                .collect(Collectors.toList());
    }

    private void login(SecurityCredentials sc, HttpSession session) {
        User user = getUser(sc);
        session.setAttribute(CREDENTIALS, sc);
        session.setAttribute(LAST_NAME, user.getLastName());
        setRole(session, sc);
    }

    private void setRole(HttpSession session, SecurityCredentials sc) {
        if (security.isCustomer(sc))
            session.setAttribute(ROLE, CUSTOMER_ATTRIBUTE);
        else if (security.isEmployee(sc))
            session.setAttribute(ROLE, EMPLOYEE_ATTRIBUTE);
        else
            throw new UnauthorizedException();
    }

    private User getUser(SecurityCredentials sc) {
        if (security.isCustomer(sc))
            return customers.getById(sc.getId(), sc);
        if (security.isEmployee(sc))
            return employees.getById(sc.getId(), sc);
        throw new UnauthorizedException();
    }

    private Customer create(CustomerDto dto) {
        Customer input = mapper.fromDto(dto);
        return customers.create(input);
    }

    private void setCredentialsTo(HttpSession session, Customer result) {
        SecurityCredentials sc = buildCredential(result);
        session.setAttribute(CREDENTIALS, sc);
    }

    private void setCredentialsTo(Model model, HttpSession session) {
        model.addAttribute(CREDENTIALS,
                session.getAttribute(CREDENTIALS) == null ?
                        new SecurityCredentials() :
                        getCredential(session));
    }
}
