package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.Employee;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;
import com.telerikacademy.web.deliver_it.models.dto.EmployeeDto;
import com.telerikacademy.web.deliver_it.models.dto.IdDto;
import com.telerikacademy.web.deliver_it.services.contracts.EmployeeService;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.EmployeeMapper;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;
import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.ACCESS_DENIED;
import static com.telerikacademy.web.deliver_it.controllers.mvc.MvcHelpers.*;
import static com.telerikacademy.web.deliver_it.utils.Builders.buildDto;

@Controller
@RequestMapping(EMPLOYEES_URL)
public class EmployeeMvcController {

    private final EmployeeService service;
    private final EmployeeMapper mapper;
    private final Security security;

    public EmployeeMvcController(EmployeeService service, Security security, EmployeeMapper mapper) {
        this.security = security;
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(BASE_URL)
    public String showEmployeeIndex(HttpSession session, Model model) {
        try {
            validateEmployee(session, security);
            IdDto dto = new IdDto();
            dto.setOptions(EMPLOYEE_OPTIONS);
            model.addAttribute(ID_DTO, dto);
            return EMPLOYEE_INDEX_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(BASE_URL)
    public String doOption(@ModelAttribute(ID_DTO) IdDto dto, HttpSession session, Model model) {
        try {
            validateEmployee(session, security);
            switch (dto.getOption()) {
                case BY_ID:
                    return getById(dto.getId(), session, model);
                case UPDATE_ID:
                    return showUpdatePage(dto.getId(), model, session);
                default:
                    return NOT_FOUND_VIEW;
            }
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(GET_BY_ID_URL)
    public String getById(@PathVariable int id, HttpSession session, Model model) {
        try {
            SecurityCredentials sc = getCredential(session);
            Employee employee = service.getById(id, sc);
            model.addAttribute(EMPLOYEE_ATTRIBUTE, employee);
            return EMPLOYEE_BY_ID_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(CREATE_URL)
    public String showCreationPage(Model model, HttpSession session) {
        try {
            getValidatedEmployeeCredential(session, security);
            model.addAttribute(EMPLOYEE_DTO_ATTRIBUTE, new EmployeeDto());
            return EMPLOYEE_CREATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(CREATE_URL)
    public String create(@Valid @ModelAttribute(EMPLOYEE_DTO_ATTRIBUTE) EmployeeDto dto,
                         BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors())
            return EMPLOYEE_CREATE_VIEW;
        try {
            SecurityCredentials sc = getCredential(session);
            Employee input = mapper.fromDto(dto);
            Employee result = service.create(input, sc);

            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("firstName", "employee.exists", e.getMessage());
            return EMPLOYEE_CREATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(UPDATE_URL)
    public String showUpdatePage(@PathVariable int id, Model model, HttpSession session) {
        try {
            SecurityCredentials sc = getCredential(session);
            Employee employee = service.getById(id, sc);
            EmployeeDto dto = buildDto(employee);
            model.addAttribute(EMPLOYEE_DTO_ATTRIBUTE, dto);

            return EMPLOYEE_UPDATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(UPDATE_URL)
    public String update(@Valid @ModelAttribute(EMPLOYEE_DTO_ATTRIBUTE) EmployeeDto dto,
                         @PathVariable int id, HttpSession session,
                         BindingResult errors, Model model) {
        if (errors.hasErrors())
            return EMPLOYEE_UPDATE_VIEW;

        try {
            Employee result = update(dto, id, session);
            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("firstName", "employee.exists", e.getMessage());
            return EMPLOYEE_UPDATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(DELETE_URL)
    public String showDeletePage(@PathVariable int id, Model model, HttpSession session) {
        try {
            SecurityCredentials sc = getCredential(session);
            Employee deleted = service.delete(id, sc);

            model.addAttribute(EMPLOYEE_ATTRIBUTE, deleted);
            if (hasMach(sc, deleted))
                return "redirect:/logout";
            return EMPLOYEE_DELETE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    private Employee update(EmployeeDto dto, int id, HttpSession session) {
        SecurityCredentials sc = getValidatedEmployeeCredential(session, security);

        Employee input = mapper.fromDto(id, dto);
        Employee result = service.update(input, sc);
        if (hasMach(sc, result))
            edit(session, result);
        return result;
    }

    private void edit(HttpSession session, Employee employee) {
        SecurityCredentials sc = new SecurityCredentials();
        sc.setName(employee.getFirstName());
        sc.setPassword(employee.getPassHash());
        session.setAttribute(CREDENTIALS, sc);
    }

    private boolean hasMach(SecurityCredentials sc, Employee deleted) {
        return sc.getId() == deleted.getId() &&
                sc.getName().equals(deleted.getFirstName());
    }

    private String viewById(IdentifyAble result) {
        return String.format("redirect:/employees/%d", result.getId());
    }
}
