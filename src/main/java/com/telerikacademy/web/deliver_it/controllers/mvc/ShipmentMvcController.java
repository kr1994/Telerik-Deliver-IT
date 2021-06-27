package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.IdDto;
import com.telerikacademy.web.deliver_it.models.dto.ShipmentDto;
import com.telerikacademy.web.deliver_it.services.contracts.ShipmentService;
import com.telerikacademy.web.deliver_it.services.contracts.ShipmentStatusService;
import com.telerikacademy.web.deliver_it.services.contracts.WarehouseService;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.ShipmentMapper;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.*;
import static com.telerikacademy.web.deliver_it.controllers.mvc.MvcHelpers.*;
import static com.telerikacademy.web.deliver_it.utils.Builders.buildDto;

@Controller
@RequestMapping(SHIPMENTS_URL)
public class ShipmentMvcController {

    private final ShipmentStatusService statusService;
    private final WarehouseService warehouses;
    private final ShipmentService service;
    private final ShipmentMapper mapper;
    private final Security security;

    @Autowired
    public ShipmentMvcController(ShipmentService service, ShipmentStatusService statusService,
                                 WarehouseService warehouses, ShipmentMapper mapper, Security security) {
        this.statusService = statusService;
        this.warehouses = warehouses;
        this.security = security;
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(BASE_URL)
    public String showOptionPage(HttpSession session, Model model) {
        try {
            validateEmployee(session, security);
            setModelForOptionsPage(model);
            return SHIPMENTS_INDEX_VIEW;
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
                    return showNextShipmentById(dto.getId(), session, model);
                case BY_USER:
                    return showAllShipmentsByUser(dto.getId(), session, model);
                case TO_WAREHOUSE:
                    return showAllShipmentsToWarehouse(dto.getId(), session, model);
                case NEXT_TO_WAREHOUSE:
                    return showNextShipmentToWarehouse(dto.getId(), session, model);
                default:
                    return NOT_FOUND_VIEW;
            }
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(SHIPMENTS_BY_USER_URL)
    public String showAllShipmentsByUser(@PathVariable int id, HttpSession session, Model model) {
        try {
            setShipmentsByUserTo(model, id, session);
            return SHIPMENT_BY_USER_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(SHIPMENTS_TO_WAREHOUSE_URL)
    public String showAllShipmentsToWarehouse(@PathVariable int id, HttpSession session, Model model) {
        try {
            setShipmentsByWarehouseTo(model, id, session);
            return SHIPMENT_BY_WAREHOUSE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(NEXT_SHIPMENT_TO_WAREHOUSE_URL)
    public String showNextShipmentToWarehouse(@PathVariable int id, HttpSession session, Model model) {
        try {
            setNextShipmentTo(model, id, session);
            return SHIPMENT_NEXT_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(GET_BY_ID_URL)
    public String showNextShipmentById(@PathVariable int id, HttpSession session, Model model) {
        try {
            setShipmentByIdTo(model, id, session);
            return SHIPMENT_BY_ID_VIEW;
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
            setModelForCreate(model, session);
            return SHIPMENT_CREATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(CREATE_URL)
    public String create(@Valid @ModelAttribute(SHIPMENT_ATTRIBUTE) ShipmentDto dto,
                         BindingResult errors, HttpSession session, Model model) {
        try {
            SecurityCredentials sc = getCredential(session);
            model.addAttribute(SHIPMENT_STATUSES, populateStatuses(sc));
            if (errors.hasErrors())
                return SHIPMENT_CREATE_VIEW;

            Shipment input = mapper.fromDto(dto);
            Shipment result = service.create(input, sc);

            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("arrivalDate", "shipment.exists", e.getMessage());
            return SHIPMENT_CREATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(UPDATE_URL)
    public String showUpdatePage(@PathVariable int id, HttpSession session, Model model) {
        try {
            setModelForUpdate(model, id, session);
            return SHIPMENT_UPDATE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @PostMapping(UPDATE_URL)
    public String update(@Valid @ModelAttribute(SHIPMENT_ATTRIBUTE) ShipmentDto dto,
                         @PathVariable int id, HttpSession session,
                         BindingResult errors, Model model) {
        try {
            SecurityCredentials sc = getCredential(session);
            model.addAttribute(SHIPMENT_STATUSES, populateStatuses(sc));
            if (errors.hasErrors())
                return SHIPMENT_UPDATE_VIEW;

            Shipment input = mapper.fromDto(id, dto);
            Shipment result = service.update(input, sc);

            return viewById(result);
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("arrivalDate", "shipment.exists", e.getMessage());
            return SHIPMENT_UPDATE_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @GetMapping(DELETE_URL)
    public String showDeletePage(@PathVariable int id, HttpSession session, Model model) {
        try {
            delete(id, session, model);
            return SHIPMENT_DELETE_VIEW;
        } catch (EntityNotFoundException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return NOT_FOUND_VIEW;
        } catch (UnauthorizedException e) {
            return ACCESS_DENIED;
        }
    }

    @ModelAttribute(WAREHOUSES_ATTRIBUTE)
    public List<Warehouse> populateWarehouses() {
        return warehouses.getAll();
    }

    private List<ShipmentStatus> populateStatuses(SecurityCredentials sc) {
        return statusService.getAll(sc);
    }

    private String viewById(Shipment result) {
        return String.format("redirect:/shipments/%d", result.getId());
    }

    private void setShipmentsByUserTo(Model model, int customerId, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        List<Shipment> byCustomer = service.getByCustomer(customerId, sc);
        model.addAttribute(SHIPMENTS_ATTRIBUTE, byCustomer);
    }

    private void setModelForOptionsPage(Model model) {
        IdDto dto = new IdDto();
        dto.setOptions(SHIPMENT_OPTIONS);
        model.addAttribute(ID_DTO, dto);
    }

    private void setShipmentsByWarehouseTo(Model model, int warehouseId, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        List<Shipment> byWarehouse = service.getByStatus(warehouseId, Optional.empty(), sc);
        model.addAttribute(SHIPMENTS_ATTRIBUTE, byWarehouse);
    }

    private void setNextShipmentTo(Model model, int WarehouseId, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        Shipment next = service.getNext(WarehouseId, sc);
        model.addAttribute(SHIPMENT_ATTRIBUTE, next);
    }

    private void setShipmentByIdTo(Model model, int shipmentId, HttpSession session) {
        SecurityCredentials sc = getCredential(session);
        Shipment byId = service.getById(shipmentId, sc);
        model.addAttribute(SHIPMENT_ATTRIBUTE, byId);
    }

    private void setModelForCreate(Model model, HttpSession session) {
        validateEmployee(session, security);
        SecurityCredentials sc = getCredential(session);
        model.addAttribute(SHIPMENT_STATUSES, populateStatuses(sc));
        model.addAttribute(SHIPMENT_ATTRIBUTE, new ShipmentDto());
        model.addAttribute(CURRENT_DATE, new Date());
    }

    private void setModelForUpdate(Model model, int ShipmentId, HttpSession session) {
        validateEmployee(session, security);
        SecurityCredentials sc = getCredential(session);
        model.addAttribute(SHIPMENT_STATUSES, populateStatuses(sc));

        Shipment stock = service.getById(ShipmentId, sc);
        ShipmentDto dto = buildDto(stock);
        model.addAttribute(SHIPMENT_ATTRIBUTE, dto);
    }

    private void delete(int shipmentId, HttpSession session, Model model) {
        SecurityCredentials sc = getCredential(session);
        Shipment deleted = service.delete(shipmentId, sc);
        model.addAttribute(SHIPMENT_ATTRIBUTE, deleted);
    }
}
