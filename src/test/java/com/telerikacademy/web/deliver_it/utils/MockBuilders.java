package com.telerikacademy.web.deliver_it.utils;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.models.dto.out.ShipmentDtoOut;
import org.springframework.http.HttpHeaders;

import java.util.*;

import static com.telerikacademy.web.deliver_it.utils.Builders.*;
import static com.telerikacademy.web.deliver_it.utils.Formatters.getDate;
import static com.telerikacademy.web.deliver_it.utils.Templates.*;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;

public abstract class MockBuilders {

    public static City mockCity() {
        City city = buildCity(CITY_NAME, COUNTRY_NAME);
        city.setId(MOCK_CITY_ID);
        city.setCountry(mockCountry());
        return city;
    }

    public static Country mockCountry() {
        Country country = buildCountry(COUNTRY_NAME);
        country.setId(MOCK_COUNTRY_ID);
        return country;
    }

    public static Address mockAddress() {
        Address address = buildAddress(STR_1, STR_2, POST);
        address.setId(MOCK_ADDRESS_ID);
        address.setCity(mockCity());
        return address;
    }

    public static Customer mockCustomer() {
        Customer customer = new Customer();
        mockCustomerInfo(customer);
        customer.setDelivery(mockAddress());
        return customer;
    }

    public static Employee mockEmployeeFromUserInput() {
        Employee employee = new Employee();
        mockEmployeeInfo(employee);
        employee.setSalt(EMPLOYEE_SALT);
        employee.setPassHash(EMPLOYEE_PASS);
        return employee;
    }

    public static Employee mockEmployeeFromRepo() {
        Employee employee = new Employee();
        mockEmployeeInfo(employee);
        employee.setSalt(EMPLOYEE_SALT);
        employee.setPassHash(EMPLOYEE_PASS_HASH);
        return employee;
    }

    public static Warehouse mockWarehouse() {
        Address address = buildAddress(WH_STR_1, WH_STR_2, WH_POST);
        address.setId(MOCK_WH_ADDRESS_ID);
        address.setCity(buildCity(WH_CITY_NAME, WH_COUNTRY));
        address.getCity().getCountry().setId(WH_COUNTRY_ID);
        address.getCity().setId(WH_CITY_ID);
        Warehouse wh = buildWarehouse(address);
        wh.setId(MOCK_WAREHOUSE_ID);
        return wh;
    }

    public static Shipment mockShipment() {
        Shipment shipment = new Shipment();
        shipment.setId(MOCK_SHIPMENT_ID);
        shipment.setDestination(mockWarehouse());
        shipment.setDepartureDate(getDate(MOCK_SHIPMENT_DEPARTURE_DATE));
        shipment.setArrivalDate(getDate(MOCK_SHIPMENT_ARRIVAL_DATE));
        shipment.setStatus(mockStatus());
        shipment.setParcels(new HashSet<>());
        shipment.getParcels().add(mockParcel());
        return shipment;
    }

    public static Parcel mockParcel() {
        Parcel parcel = new Parcel();
        parcel.setId(MOCK_PARCEL_ID);
        parcel.setCustomer(mockCustomer());
        parcel.setWeight(MOCK_PARCEL_WEIGHT);
        parcel.setWeightUnit(mockWeightUnits());
        parcel.setDestination(mockWarehouse());
        parcel.setCategory(mockCategory());
        parcel.setShipmentId(MOCK_SHIPMENT_ID);
        return parcel;
    }

    public static ShipmentStatus mockStatus() {
        ShipmentStatus ss = new ShipmentStatus();
        ss.setStatus(MOCK_SHIPMENT_STATUS);
        ss.setId(MOCK_SHIPMENT_STATUS_ID);
        return ss;
    }

    public static ParcelCategory mockCategory() {
        ParcelCategory pc = new ParcelCategory();
        pc.setId(MOCK_PARCEL_CATEGORY_ID);
        pc.setParcelCategory(MOCK_PARCEL_CATEGORY);
        return pc;
    }

    public static WeightUnit mockWeightUnits() {
        WeightUnit wu = new WeightUnit();
        wu.setId(MOCK_UNITS_ID);
        wu.setUnits(MOCK_UNITS);
        wu.setFactor(MOCK_PARCEL_WEIGHT_FACTOR);
        return wu;
    }

    public static HttpHeaders mockCustomerHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY_ID, "" + MOCK_CUSTOMER_ID);
        headers.add(HEADER_KEY_NAME, CUSTOMER_FIRST_NAME);
        return headers;
    }

    public static HttpHeaders mockEmployeeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY_ID, "" + MOCK_EMPLOYEE_ID);
        headers.add(HEADER_KEY_NAME, EMPLOYEE_FIRST_NAME);
        headers.add(HEADER_KEY_PASS, EMPLOYEE_PASS);
        return headers;
    }

    public static SecurityCredentials mockSecurityCredentialCustomer() {
        SecurityCredentials sc = new SecurityCredentials();
        sc.setId(MOCK_CUSTOMER_ID);
        sc.setName(CUSTOMER_FIRST_NAME);
        return sc;
    }

    public static SecurityCredentials mockSecurityCredentialEmployee() {
        SecurityCredentials sc = new SecurityCredentials();
        sc.setId(MOCK_EMPLOYEE_ID);
        sc.setName(EMPLOYEE_FIRST_NAME);
        sc.setPassword(EMPLOYEE_PASS);
        return sc;
    }

    public static SecurityCredentials mockSecurityCredentialsWrong() {
        SecurityCredentials sc = mockSecurityCredentialCustomer();
        sc.setName("Wrong");
        sc.setId(-3);
        return sc;
    }

    public static List<ParcelDtoOut> mockDtoFromMapperWithWeight(double w1, double w2, double w3) {
        ParcelDtoOut p1 = mockParcelDtoOut();
        p1.setWeight(w1);
        ParcelDtoOut p2 = mockParcelDtoOut();
        p2.setWeight(w2);
        ParcelDtoOut p3 = mockParcelDtoOut();
        p3.setWeight(w3);
        return List.of(p1, p2, p3);
    }

    public static List<ParcelDtoOut> mockDtoFromMapperWithArrivalDate(String d1, String d2, String d3) {
        ParcelDtoOut p1 = mockParcelDtoOut();
        p1.setArrivalDate(d1);
        ParcelDtoOut p2 = mockParcelDtoOut();
        p2.setArrivalDate(d2);
        ParcelDtoOut p3 = mockParcelDtoOut();
        p3.setArrivalDate(d3);
        return List.of(p1, p2, p3);
    }

    public static List<Shipment> mockShipmentsFromRepoWithWeight(double w1, double w2, double w3) {
        Parcel p1 = mockParcel();
        p1.setWeight(w1);
        Parcel p2 = mockParcel();
        p2.setWeight(w2);
        Parcel p3 = mockParcel();
        p3.setWeight(w3);
        Shipment sh = mockShipment();
        sh.setParcels(Set.of(p1, p2, p3));
        return List.of(sh);
    }

    public static List<Shipment> mockShipmentsFromRepoWithArrivalDate(String d1, String d2, String d3) {
        Shipment sh1 = mockShipment();
        sh1.setArrivalDate(getDate(d1));
        Shipment sh2 = mockShipment();
        sh2.setArrivalDate(getDate(d2));
        Shipment sh3 = mockShipment();
        sh3.setArrivalDate(getDate(d3));
        return List.of(sh1, sh2, sh3);
    }

    public static ParcelDtoOut mockParcelDtoOut() {
        return new ParcelDtoOut(
                MOCK_PARCEL_ID,
                MOCK_CUSTOMER_ID,
                CUSTOMER_FIRST_NAME,
                MOCK_WAREHOUSE_ID,
                WH_CITY_NAME,
                MOCK_PARCEL_CATEGORY,
                MOCK_PARCEL_WEIGHT,
                MOCK_UNITS,
                MOCK_SHIPMENT_ID,
                getDate(MOCK_SHIPMENT_ARRIVAL_DATE),
                MOCK_SHIPMENT_STATUS,
                MOCK_UNITS_FACTOR,
                MOCK_WAREHOUSE_ID
        );
    }

    public static ShipmentDtoOut mockShipmentDtoOut() {
        return new ShipmentDtoOut(
                MOCK_SHIPMENT_ID,
                MOCK_SHIPMENT_STATUS,
                getDate(MOCK_SHIPMENT_DEPARTURE_DATE),
                getDate(MOCK_SHIPMENT_ARRIVAL_DATE),
                MOCK_WAREHOUSE_ID,
                WH_CITY_NAME,
                List.of(MOCK_PARCEL_ID));
    }

    public static FilterByWeightDto mockFilterByWeightDto(){
        FilterByWeightDto dto = new FilterByWeightDto();
        dto.setMinWeight(MOCK_WEIGHT_FILTER_DTO_MIN_WEIGHT);
        dto.setMinWeightFactor(MOCK_WEIGHT_FILTER_DTO_FACTOR);
        dto.setMinUnit(MOCK_UNITS);

        dto.setMaxWeight(MOCK_WEIGHT_FILTER_DTO_MAX_WEIGHT);
        dto.setMaxWeightFactor(MOCK_WEIGHT_FILTER_DTO_FACTOR);
        dto.setMaxUnit(MOCK_UNITS);
        return dto;
    }

    private static void mockCustomerInfo(Customer customer) {
        fillUserInfo(customer, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_EMAIL);
        customer.setId(MOCK_CUSTOMER_ID);
    }

    private static void mockEmployeeInfo(Employee employee) {
        fillUserInfo(employee, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_EMAIL);
        employee.setId(MOCK_EMPLOYEE_ID);
    }
}
