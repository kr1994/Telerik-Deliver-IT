package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.models.Parcel;
import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.repositories.contracts.ParcelRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentRepository;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoOutMappers;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.Filter;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.telerikacademy.web.deliver_it.utils.Formatters.isInvalidWeight;

@Service
public class FilterImpl implements Filter {

    private final ShipmentRepository shipments;
    private final ParcelRepository parcels;
    private final DtoOutMappers mappersOut;

    @Autowired
    public FilterImpl(ParcelRepository parcels, DtoOutMappers mappersOut, ShipmentRepository shipments) {
        this.mappersOut = mappersOut;
        this.shipments = shipments;
        this.parcels = parcels;
    }

    @Override
    public List<ParcelDtoOut> byStatusMapToDto(int statusId) {
        List<Parcel> result = parcels.getAllBy(statusId);
        return mappersOut.mapToDto(result);
    }

    @Override
    public List<ParcelDtoOut> customers_sOwnBy(int statusId, int customerId) {
        List<Parcel> result = parcels.getAllBy(customerId, statusId);
        return mappersOut.mapToDto(result);
    }

    @Override
    public List<ParcelDtoOut> toDtoBy(FilterByWeightDto dto) {
        List<Parcel> result = parcels.filterByWeight(dto);
        return mappersOut.mapToDto(result);
    }

    @Override
    public List<ParcelDtoOut> customers_sOwnBy(FilterByWeightDto weightFilterDto, int customerId) {
        ParcelSortingDto parcelSortDto = new ParcelSortingDto();
        parcelSortDto.setCustomerId(customerId);

        List<Shipment> fromRepo = shipments.getAll(parcelSortDto);
        List<ParcelDtoOut> toDto = mappersOut.mapToParcelsDto(fromRepo);

        return filterBy(weightFilterDto, toDto);
    }

    @Override
    public ParcelDtoOut byCustomerAndParcel(int customerId, int parcelId) {
        Parcel parcel = getParcelIfCustomer_sOwn(parcelId, customerId);
        return mappersOut.mapToDtoFromRepo(parcel);
    }

    @Override
    public Parcel getParcelIfCustomer_sOwn(int parcelId, int customerId) {
        try {
            Parcel parcel = parcels.getById(parcelId);
            if (isCustomers_s(parcel, customerId))
                return parcel;

        } catch (EntityNotFoundException ignored) {
        }
        throw new UnauthorizedException();
    }

    private List<ParcelDtoOut> filterBy(FilterByWeightDto dto, List<ParcelDtoOut> result) {
        return result.stream()
                .filter(p -> isInvalidWeight(dto.getMaxWeight()) ||
                        p.getWeight() * p.getWeightFactor() <= dto.getMaxWeight() * dto.getMaxWeightFactor())
                .filter(p -> isInvalidWeight(dto.getMinWeight()) ||
                        p.getWeight() * p.getWeightFactor() >= dto.getMinWeight() * dto.getMinWeightFactor())
                .collect(Collectors.toList());
    }

    private boolean isCustomers_s(Parcel parcel, int customerId) {
        return parcel.getCustomer().getId() == customerId;
    }

}
