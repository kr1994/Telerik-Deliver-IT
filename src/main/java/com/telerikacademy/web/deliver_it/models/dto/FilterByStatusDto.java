package com.telerikacademy.web.deliver_it.models.dto;

import com.telerikacademy.web.deliver_it.models.ShipmentStatus;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import java.util.ArrayList;
import java.util.List;

public class FilterByStatusDto implements IdentifyAble {

    int id;

    List<ShipmentStatus> statuses = new ArrayList<>();

    public FilterByStatusDto() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public List<ShipmentStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<ShipmentStatus> statuses) {
        this.statuses = statuses;
    }
}
