package com.telerikacademy.web.deliver_it.repositories.contracts;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends Repository<Parcel> {

    List<Parcel> getAllBy(int customerId, int statusId);

    List<Parcel> getAllBy(int statusId);

    List<Parcel> filterByWeight(FilterByWeightDto dto);
}
