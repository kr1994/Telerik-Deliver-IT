package com.telerikacademy.web.deliver_it.controllers.rest;

import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.ShipmentMapper;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.dto.ShipmentDto;
import com.telerikacademy.web.deliver_it.services.contracts.ShipmentService;
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
@RequestMapping("/deliver_it/shipments")
public class ShipmentController {

    private final ShipmentService service;
    private final ShipmentMapper mapper;

    @Autowired
    public ShipmentController(ShipmentService service, ShipmentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/customers/{id}")
    public List<Shipment> getByCustomer(@Positive @PathVariable int id,
                                        @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.getByCustomer(id, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/warehouses/{id}")
    public List<Shipment> getByWarehouseByStatus(@Positive @PathVariable int id,
                                                 @RequestParam(required = false) Optional<Integer> statusId,
                                                 @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.getByStatus(id, statusId, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @GetMapping("/next")
    public Shipment getNext(@Positive @RequestParam int warehouseId,
                            @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.getNext(warehouseId, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Shipment getById(@Positive @PathVariable int id,
                            @RequestHeader HttpHeaders header) {
        return ControllerHelper.getById(id, header, service);
    }

    @PostMapping
    public Shipment create(@Valid @RequestBody ShipmentDto shipmentDto,
                           @RequestHeader HttpHeaders header) {
        Shipment shipment = fromDto(shipmentDto);
        return ControllerHelper.create(shipment,header,service);
    }

    @PutMapping("/{id}")
    public Shipment update(@Positive @PathVariable int id,
                           @Valid @RequestBody ShipmentDto shipmentDto,
                           @RequestHeader HttpHeaders header) {
        Shipment shipment = fromDto(id, shipmentDto);
        return ControllerHelper.update(shipment,header,service);
    }

    @DeleteMapping("/{id}")
    public Shipment delete(@Positive @PathVariable int id,
                           @RequestHeader HttpHeaders header) {
        return ControllerHelper.delete(id, header, service);
    }

    private SecurityCredentials getCredential(HttpHeaders header) {
        return buildCredential(header);
    }

    private Shipment fromDto(ShipmentDto shipmentDto) {
        try {
            return mapper.fromDto(shipmentDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private Shipment fromDto(int id, ShipmentDto shipmentDto) {
        try {
            return mapper.fromDto(id, shipmentDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
