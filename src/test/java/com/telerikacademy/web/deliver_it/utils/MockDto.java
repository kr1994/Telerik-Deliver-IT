package com.telerikacademy.web.deliver_it.utils;

import com.telerikacademy.web.deliver_it.models.dto.*;

import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.Formatters.getDate;

public abstract class MockDto {

    public static AddressDto buildCustomerAddressDto() {
        AddressDto dto = new AddressDto();
        dto.setPostCode(POST);
        dto.setStreet1(STR_1);
        dto.setStreet2(STR_2);
        dto.setCityId(MOCK_CITY_ID);
        return dto;
    }

    public static AddressDto buildWarehouseAddressDto() {
        AddressDto dto = new AddressDto();
        dto.setPostCode(WH_POST);
        dto.setStreet1(WH_STR_1);
        dto.setStreet2(WH_STR_2);
        dto.setCityId(WH_CITY_ID);
        return dto;
    }

    public static ParcelDto buildParcelDto() {
        ParcelDto dto = new ParcelDto();
        dto.setCustomerId(MOCK_CUSTOMER_ID);
        dto.setWarehouseId(MOCK_WAREHOUSE_ID);
        dto.setParcelCategoryId(MOCK_PARCEL_CATEGORY_ID);
        dto.setWeight(MOCK_PARCEL_WEIGHT);
        dto.setWeightUnitsId(MOCK_UNITS_ID);
        dto.setShipmentId(MOCK_SHIPMENT_ID);
        return dto;
    }

    public static CustomerDto buildCustomerDto() {
        CustomerDto dto = new CustomerDto();
        dto.setEmail(CUSTOMER_EMAIL);
        dto.setFirstName(CUSTOMER_FIRST_NAME);
        dto.setLastName(CUSTOMER_LAST_NAME);
        dto.setAddressId(MOCK_ADDRESS_ID);
        return dto;
    }

    public static ShipmentDto buildShipmentDto() {
        ShipmentDto dto = new ShipmentDto();
        dto.setArrivalDate(getDate(MOCK_SHIPMENT_ARRIVAL_DATE));
        dto.setDepartureDate(getDate(MOCK_SHIPMENT_DEPARTURE_DATE));
        dto.setWarehouseId(MOCK_WAREHOUSE_ID);
        dto.setShipmentStatusId(MOCK_SHIPMENT_STATUS_ID);
        return dto;
    }

    public static EmployeeDto buildEmployeeDto() {
        EmployeeDto dto = new EmployeeDto();
        dto.setPassword(EMPLOYEE_PASS);
        dto.setEmail(EMPLOYEE_EMAIL);
        dto.setFirstName(EMPLOYEE_FIRST_NAME);
        dto.setLastName(EMPLOYEE_LAST_NAME);
        return dto;
    }
}
