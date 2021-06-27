package com.telerikacademy.web.deliver_it.utils.enums;

import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;

public enum ShipmentStatusValues {
    ORDERED,
    SENT,
    SHIPPING,
    DELIVERED,
    CLOSED;

    @Override
    public String toString() {
        switch (this){
            case ORDERED:
                return "Ordered";
            case SENT:
                return "Sent";
            case SHIPPING:
                return "Shipping";
            case DELIVERED:
                return "Delivered";
            case CLOSED:
                return "Closed";
            default:
                throw new EntityNotFoundException("Unsupported shipment status");
        }
    }
}
