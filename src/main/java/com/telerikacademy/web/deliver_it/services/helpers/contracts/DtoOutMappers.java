package com.telerikacademy.web.deliver_it.services.helpers.contracts;

import com.telerikacademy.web.deliver_it.models.Parcel;
import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.models.dto.out.ShipmentDtoOut;

import java.util.List;

public interface DtoOutMappers {

    ParcelDtoOut mapToParcelDto(Parcel parcel, Shipment shipment);

    ParcelDtoOut mapToDtoFrom(Parcel parcel);

    ParcelDtoOut mapToDtoFromRepo(Parcel parcel);

    List<ParcelDtoOut> mapToDto(List<Parcel> parcels);

    List<ParcelDtoOut> mapToParcelsDto(List<Shipment> input);

    ShipmentDtoOut mapToDto(Shipment shipment);
}
