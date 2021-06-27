package com.telerikacademy.web.deliver_it.services.helpers.contracts;

import com.telerikacademy.web.deliver_it.models.Parcel;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;

import java.util.List;
import java.util.Optional;

public interface Filter {
    List<ParcelDtoOut> byStatusMapToDto(int statusId);

    List<ParcelDtoOut> customers_sOwnBy(int statusId, int customerId);

    List<ParcelDtoOut> toDtoBy(FilterByWeightDto dto);

    List<ParcelDtoOut> customers_sOwnBy( FilterByWeightDto dto,
                                        int customerId);

    ParcelDtoOut byCustomerAndParcel(int customerId, int parcelId);

    Parcel getParcelIfCustomer_sOwn(int parcelId, int customerId);
}
