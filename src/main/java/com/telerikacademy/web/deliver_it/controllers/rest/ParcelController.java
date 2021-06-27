package com.telerikacademy.web.deliver_it.controllers.rest;

import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.sub.ParcelMapper;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import com.telerikacademy.web.deliver_it.services.contracts.ParcelService;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.dto.ParcelDto;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import com.telerikacademy.web.deliver_it.models.Parcel;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.buildCredential;
import static com.telerikacademy.web.deliver_it.utils.Builders.buildDto;

@RestController
@RequestMapping("/deliver_it/parcels")
public class ParcelController {

    private final ParcelService service;
    private final ParcelMapper mapper;

    @Autowired
    public ParcelController(ParcelService service, ParcelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ParcelDtoOut> getAll(@RequestParam(required = false) Integer customerId,
                                     @RequestParam(required = false) Integer warehouseId,
                                     @RequestParam(required = false) Double weight,
                                     @RequestParam(required = false) Integer categoryId,
                                     @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            ParcelSortingDto dto = buildDto(customerId, warehouseId, weight, categoryId);
            return service.getAllSorted(dto, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/sorted")
    public List<ParcelDtoOut> getAllSorted(@RequestParam(required = false) Integer customerId,
                                           @RequestParam(required = false) Integer warehouseId,
                                           @RequestParam(required = false) Double weight,
                                           @RequestParam(required = false) Integer categoryId,
                                           @RequestParam(required = false) String weightSort,
                                           @RequestParam(required = false) String arrivalDateSort,
                                           @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            ParcelSortingDto dto = buildDto(customerId, warehouseId, weight, categoryId, weightSort,arrivalDateSort);
            return service.getAllSorted(dto, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/status")
    public List<ParcelDtoOut> getByStatusOfShipment(@Positive @RequestParam int statusId,
                                                    @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.filterByStatusFromShipment(statusId, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/status")
    public ParcelDtoOut getStatusOfParcel(@Positive @PathVariable int id,
                                          @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.getStatusOfParcel(id, sc);

        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/weight")
    public List<ParcelDtoOut> filterByWeight(@RequestParam(required = false) Double min,
                                             @RequestParam(required = false) String minUnits,
                                             @RequestParam(required = false) Double max,
                                             @RequestParam(required = false) String maxUnits,
                                             @RequestHeader HttpHeaders header) {
        try {
            SecurityCredentials sc = getCredential(header);
            FilterByWeightDto dto = new FilterByWeightDto();
            dto.setMinWeight(min);
            dto.setMinUnit(minUnits);
            dto.setMinWeight(max);
            dto.setMinUnit(maxUnits);
            return service.filterByWeight(dto, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Parcel getById(@Positive @PathVariable int id,
                          @RequestHeader HttpHeaders header) {
        return ControllerHelper.getById(id, header, service);
    }

    @PostMapping
    public Parcel create(@Valid @RequestBody ParcelDto dto,
                         @RequestHeader HttpHeaders header) {
        Parcel parcel = fromDto(dto);
        return ControllerHelper.create(parcel, header, service);
    }

    @PutMapping("/{id}")
    public Parcel update(@Positive @PathVariable int id,
                         @Valid @RequestBody ParcelDto dto,
                         @RequestHeader HttpHeaders header) {
        Parcel parcel = fromDto(id, dto);
        return ControllerHelper.update(parcel, header, service);
    }

    @DeleteMapping("/{id}")
    public Parcel delete(@Positive @PathVariable int id,
                         @RequestHeader HttpHeaders header) {
        return ControllerHelper.delete(id, header, service);
    }

    private SecurityCredentials getCredential(HttpHeaders header) {
        return buildCredential(header);
    }

    private Parcel fromDto(ParcelDto parcelDto) {
        try {
            return mapper.fromDto(parcelDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private Parcel fromDto(int id, ParcelDto parcelDto) {
        try {
            return mapper.fromDto(id, parcelDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
