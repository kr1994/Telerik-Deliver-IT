package com.telerikacademy.web.deliver_it.services.helpers.contracts;

import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;

import java.util.List;
import java.util.Optional;

public interface Sort {
    List<ParcelDtoOut> getAll(ParcelSortingDto dto);
}
