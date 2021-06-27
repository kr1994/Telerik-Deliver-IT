package com.telerikacademy.web.deliver_it.services.contracts;

import com.telerikacademy.web.deliver_it.models.Parcel;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.services.contracts.sub.DeleteService;

import java.util.List;
import java.util.Optional;

public interface ParcelService extends Service<Parcel> {

    List<ParcelDtoOut> getAllSorted(ParcelSortingDto dto, SecurityCredentials sc);

    List<ParcelDtoOut> filterByStatusFromShipment(int statusId,
                                                  SecurityCredentials sc);

    List<ParcelDtoOut> filterByWeight(FilterByWeightDto dto,
                                      SecurityCredentials sc);

    ParcelDtoOut getStatusOfParcel(int parcelId, SecurityCredentials sc);
}
