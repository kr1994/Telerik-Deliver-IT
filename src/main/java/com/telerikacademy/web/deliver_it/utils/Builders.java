package com.telerikacademy.web.deliver_it.utils;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.*;
import com.telerikacademy.web.deliver_it.utils.enums.ShipmentStatusValues;

import java.util.Date;
import java.util.Set;

public abstract class Builders {

    public static Address buildAddress(String str1, String str2, String postCode) {
        Address address = new Address();
        address.setStreet1(str1);
        address.setStreet2(str2);
        address.setPostCode(postCode);
        return address;
    }

    public static void fillUserInfo(User user, String firstName, String lastName, String email) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
    }

    public static City buildCity(String cityName, String countryName) {
        City city = new City();
        city.setCityName(cityName);
        city.setCountry(buildCountry(countryName));
        return city;
    }

    public static Country buildCountry(String countryName) {
        Country country = new Country();
        country.setCountryName(countryName);
        return country;
    }

    public static Warehouse buildWarehouse(Address address) {
        Warehouse warehouse = new Warehouse();
        warehouse.setAddress(address);
        return warehouse;
    }

    public static Warehouse buildEmptyWarehouse() {
        Country country = new Country();
        country.setCountryName("");
        City city = new City();
        city.setCityName("");
        city.setCountry(country);
        Address address = new Address();
        address.setStreet2("");
        address.setStreet2("");
        address.setPostCode("");
        address.setCity(city);
        Warehouse warehouse = new Warehouse();
        warehouse.setAddress(address);
        return warehouse;
    }

    public static ShipmentStatus buildStatus_Delivered() {
        ShipmentStatus status = new ShipmentStatus();
        status.setStatus(ShipmentStatusValues.DELIVERED.toString());
        status.setId(ShipmentStatusValues.DELIVERED.ordinal() + 1);
        return status;
    }

    public static Parcel buildParcel(int id, Warehouse destination, Customer owner, ParcelCategory category,
                                     double weight, WeightUnit units, int shipmentId) {
        Parcel parcel = new Parcel();
        parcel.setId(id);
        parcel.setDestination(destination);
        parcel.setCustomer(owner);
        parcel.setCategory(category);
        parcel.setWeight(weight);
        parcel.setWeightUnit(units);
        parcel.setShipmentId(shipmentId);
        return parcel;
    }

    public static Shipment buildShipment(int id, Date departure, Date arrival, Warehouse destination,
                                         ShipmentStatus status, Set<Parcel> parcels) {
        Shipment result = new Shipment();
        result.setId(id);
        result.setDepartureDate(departure);
        result.setArrivalDate(arrival);
        result.setDestination(destination);
        result.setStatus(status);
        result.setParcels(parcels);

        return result;
    }

    public static AddressDto buildDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setCityId(address.getCity().getId());
        dto.setStreet1(address.getStreet1());
        dto.setStreet2(address.getStreet2());
        dto.setPostCode(address.getPostCode());
        return dto;
    }

    public static ShipmentDto buildDto(Shipment shipment) {
        ShipmentDto dto = new ShipmentDto();
        dto.setArrivalDate(shipment.getArrivalDate());
        dto.setDepartureDate(shipment.getDepartureDate());
        dto.setShipmentStatusId(shipment.getStatus().getId());
        dto.setWarehouseId(shipment.getDestination().getId());
        return dto;
    }

    public static ParcelDto buildDto(Parcel parcel) {
        ParcelDto dto = new ParcelDto();
        dto.setWeightUnitsId(parcel.getWeightUnit().getId());
        dto.setParcelCategoryId(parcel.getCategory().getId());
        dto.setCustomerId(parcel.getCustomer().getId());
        dto.setWeight(parcel.getWeight());
        dto.setWarehouseId(parcel.getDestination().getId());
        dto.setShipmentId(parcel.getShipmentId());
        return dto;
    }

    public static CustomerDto buildDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setAddressId(customer.getDelivery().getId());
        return dto;
    }

    public static EmployeeDto buildDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        return dto;
    }

    public static ParcelSortingDto buildDto(Integer customerId, Integer warehouseId, Double weight,
                                            Integer categoryId, String weightSort, String arrivalDateSort) {
        ParcelSortingDto dto = new ParcelSortingDto();
        dto.setCustomerId(customerId);
        dto.setWarehouseId(warehouseId);
        dto.setWeight(weight);
        dto.setParcelCategoryId(categoryId);
        dto.setWeightSort(weightSort);
        dto.setArrivalDateSort(arrivalDateSort);
        return dto;
    }

    public static ParcelSortingDto buildDto(Integer customerId, Integer warehouseId,
                                            Double weight, Integer categoryId) {
        return buildDto(customerId,warehouseId,weight,categoryId,"","");
    }
}
