package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.Filter;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.Sort;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import com.telerikacademy.web.deliver_it.repositories.contracts.*;
import com.telerikacademy.web.deliver_it.services.contracts.ParcelService;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParcelServiceImpl extends ServiceBase<Parcel> implements ParcelService {

    private final Repository<Parcel> parcels;
    private final Filter filter;
    private final Sort sort;

    @Autowired
    public ParcelServiceImpl(ParcelRepository parcels, Security security,
                             Filter filter, Sort sort) {
        super(parcels, security);
        this.parcels = parcels;
        this.filter = filter;
        this.sort = sort;
    }

    @Override//emp or own
    public List<ParcelDtoOut> getAllSorted(ParcelSortingDto dto, SecurityCredentials sc) {
        if (isEmployee(sc))
            return sort.getAll(dto);
        if (isCustomer(sc)) {
            dto.setCustomerId(sc.getId());
            return sort.getAll(dto);
        }
        throw new UnauthorizedException();
    }

    @Override//emp or own
    public List<ParcelDtoOut> filterByStatusFromShipment(int statusId, SecurityCredentials sc) {
        if (isEmployee(sc))
            return filter.byStatusMapToDto(statusId);
        if (isCustomer(sc)) {
            return filter.customers_sOwnBy(statusId, sc.getId());
        }
        throw new UnauthorizedException();
    }

    @Override
    public List<ParcelDtoOut> filterByWeight(FilterByWeightDto dto, SecurityCredentials sc) {
        if (isEmployee(sc))
            return filter.toDtoBy(dto);
        if (isCustomer(sc)) {
            return filter.customers_sOwnBy(dto, sc.getId());
        }
        throw new UnauthorizedException();
    }

    @Override//emp or own
    public Parcel getById(int parcelId, SecurityCredentials sc) {
        if (isEmployee(sc))
            return parcels.getById(parcelId);
        if (isCustomer(sc))
            return filter.getParcelIfCustomer_sOwn(parcelId, sc.getId());
        throw new UnauthorizedException();

    }

    @Override//employee, can have duplicates by name
    public Parcel create(Parcel entity, SecurityCredentials sc) {
        //validateUnique(entity);
        if (isEmployee(sc))
            return parcels.create(entity);
        throw new UnauthorizedException();
    }

    @Override//customer only
    public ParcelDtoOut getStatusOfParcel(int parcelId, SecurityCredentials sc) {
        if (isCustomer(sc))
            return filter.byCustomerAndParcel(sc.getId(), parcelId);
        throw new UnauthorizedException();
    }
}
