package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.repositories.contracts.ReadOnlyRepository;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoOutMappers;
import com.telerikacademy.web.deliver_it.models.dto.out.ShipmentDtoOut;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.Parcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DtoOutMappersImpl implements DtoOutMappers {

    private final ReadOnlyRepository<Shipment> shipments;

    @Autowired
    public DtoOutMappersImpl(ReadOnlyRepository<Shipment> shipments) {
        this.shipments = shipments;
    }

    @Override
    public ParcelDtoOut mapToParcelDto(Parcel parcel, Shipment shipment) {
        return new ParcelDtoOut(parcel.getId(),
                parcel.getCustomer().getId(),
                parcel.getCustomer().getFirstName(),
                shipment.getDestination().getId(),
                shipment.getDestination().getCityName(),
                parcel.getCategory().getParcelCategory(),
                parcel.getWeight(),
                parcel.getWeightUnit().getUnits(),
                shipment.getId(),
                shipment.getArrivalDate(),
                shipment.getStatus().getStatus(),
                parcel.getWeightUnit().getFactor(),
                parcel.getDestination().getId()
        );
    }

    @Override
    public ParcelDtoOut mapToDtoFrom(Parcel parcel) {
        ParcelDtoOut result = new ParcelDtoOut();
        result.setId(parcel.getId());
        result.setCustomerId(parcel.getCustomer().getId());
        result.setCustomerName(parcel.getCustomer().getFirstName());
        result.setCategoryName(parcel.getCategory().getParcelCategory());
        result.setWeight(parcel.getWeight());
        result.setWeightUnits(parcel.getWeightUnit().getUnits());
        result.setWeightFactor(parcel.getWeightUnit().getFactor());
        result.setShipmentId(parcel.getShipmentId());
        result.setParcelWarehouseId(parcel.getDestination().getId());
        return result;
    }

    @Override
    public ParcelDtoOut mapToDtoFromRepo(Parcel parcel) {
        Shipment shipment = shipments.getById(parcel.getShipmentId());

        return mapToParcelDto(parcel, shipment);
    }

    @Override
    public List<ParcelDtoOut> mapToDto(List<Parcel> parcels) {
        return parcels.stream()
                .map(this::mapToDtoFromRepo)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParcelDtoOut> mapToParcelsDto(List<Shipment> input) {
        List<ParcelDtoOut> result = new ArrayList<>();
        input.forEach(sh -> sh.getParcelsList()
                .forEach(p -> result.add(mapToParcelDto(p, sh))));
        return result;
    }

    @Override
    public ShipmentDtoOut mapToDto(Shipment shipment) {
        return new ShipmentDtoOut(shipment.getId(),
                shipment.getStatus().getStatus(),
                shipment.getDepartureDate(),
                shipment.getArrivalDate(),
                shipment.getDestination().getId(),
                shipment.getDestination().getCityName(),
                getParcelIds(shipment));
    }

    private List<Integer> getParcelIds(Shipment shipment) {
        return shipment.getParcelsList().stream()
                .map(Parcel::getId)
                .collect(Collectors.toList());
    }
}
