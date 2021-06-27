package com.telerikacademy.web.deliver_it.utils;

import com.telerikacademy.web.deliver_it.models.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import static com.telerikacademy.web.deliver_it.utils.Templates.dateFormat;

public abstract class Formatters {

    public static boolean isInvalid(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isValid(String str) {
        return !isInvalid(str);
    }

    public static boolean isInvalidWeight(Double weight) {
        return weight == null || weight <= 0.0;
    }

    public static boolean isValidWeight(Double weight) {
        return !isInvalidWeight(weight);
    }

    public static boolean isInvalidId(Integer id) {
        return id == null || id == 0;
    }

    public static boolean isValidId(Integer id) {
        return !isInvalidId(id);
    }

    public static Date getDate(String yyyyMMdd_SeparatedByMinuses) {
        try {
            return dateFormat.parse(yyyyMMdd_SeparatedByMinuses);
        } catch (ParseException e) {
            throw new IllegalArgumentException("can not parse date");
        }
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static String addressPostal(Address address) {
        return String.format("%s%n" +
                        "%s%n" +
                        "%s%n" +
                        "%s%n" +
                        "%s%n",
                address.getStreet1(),
                address.getStreet2(),
                address.getCityName(),
                address.getCountryName(),
                address.getPostCode()
        );
    }

    public static String addressProperties(Address address) {
        return String.format("id: %d.%n" +
                        "Street Line 1: %s.%n" +
                        "Street line 2: %s.%n" +
                        "City: %s.%n" +
                        "Country: %s.%n" +
                        "Post code: %s",
                address.getId(),
                address.getStreet1(),
                address.getStreet2(),
                address.getCityName(),
                address.getCountryName(),
                address.getPostCode()
        );
    }

    public static String cityProperties(City city) {
        return String.format("id: %d.%n" +
                        "City name: %s.%n" +
                        "Country name: %s.",
                city.getId(),
                city.getCityName(),
                city.getCountry().getCountryName()
        );
    }

    public static String countryProperties(Country country) {
        return String.format("id: %d.%n" +
                        "Country name: %s.",
                country.getId(),
                country.getCountryName()
        );
    }

    public static String customerProperties(Customer customer) {
        return String.format("id: %s.%n" +
                        "First name: %s.%n" +
                        "Last name: %s.%n" +
                        "Email: %s.%n" +
                        "Delivery address %s",
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                addressProperties(customer.getDelivery())
        );
    }

    public static String EmployeeProperties(Employee employee) {
        return String.format("id: %d.%n" +
                        "First name: %s.%n" +
                        "Last name: %s.%n" +
                        "Email: %s.%n",
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }

    public static String parcelProperties(Parcel parcel) {
        return String.format("id: %d.%n" +
                        "Category: %s.%n" +
                        "Weight: %s" +
                        "Customer: %s.%n" +
                        "Destination warehouse %s.%n",
                parcel.getId(),
                parcel.getCategory(),
                weight(parcel),
                customerNames(parcel.getCustomer()),
                warehouseProperties(parcel.getDestination())

        );
    }

    public static String parcelProperties(Set<Parcel> parcels) {
        StringBuilder result = new StringBuilder();
        parcels.forEach(p -> result
                .append(parcelLine(p))
                .append(System.lineSeparator())
        );
        return result.toString();
    }

    public static String shipmentProperties(Shipment shipment) {
        return String.format("id: %s.%n" +
                        "Departure date: %s.%n" +
                        "Arrival date: %s.%n" +
                        "Shipment status: %s.%n" +
                        "Destination warehouse %s.%n" +
                        "Parcels:%n%s%n",
                shipment.getId(),
                dateToString(shipment.getDepartureDate()),
                dateToString(shipment.getArrivalDate()),
                shipment.getStatus(),
                warehouseProperties(shipment.getDestination()),
                parcelProperties(shipment.getParcels())
        );
    }

    public static String warehouseProperties(Warehouse warehouse) {
        return String.format("id: %d.%n" +
                        "Warehouse address %s",
                warehouse.getId(),
                addressProperties(warehouse.getAddress())
        );
    }

    public static String weight(Parcel parcel) {
        return String.format("%.2f [%s]",
                parcel.getWeight(),
                parcel.getWeightUnit().getUnits());
    }

    public static String customerNames(Customer customer) {
        return String.format("%s %s", customer.getFirstName(), customer.getLastName());
    }

    public static String parcelLine(Parcel parcel) {
        return String.format("id: %d" +
                        "category: %s" +
                        "customer: %s" +
                        "destination warehouse %s" +
                        "weight: %s.",
                parcel.getId(),
                parcel.getCategory(),
                customerNames(parcel.getCustomer()),
                warehouseProperties(parcel.getDestination()),
                weight(parcel));
    }
}
