package com.telerikacademy.web.deliver_it.services.helpers.contracts.sub;

import com.telerikacademy.web.deliver_it.models.Parcel;
import com.telerikacademy.web.deliver_it.models.dto.ParcelDto;

public interface ParcelMapper {
    Parcel fromDto(ParcelDto info);

    Parcel fromDto(int parcelId, ParcelDto info);

}
