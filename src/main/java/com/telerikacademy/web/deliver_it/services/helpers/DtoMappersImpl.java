package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.*;
import com.telerikacademy.web.deliver_it.repositories.contracts.*;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.telerikacademy.web.deliver_it.utils.Builders.buildWarehouse;

@Service
public class DtoMappersImpl implements DtoMappers {

    private final ReadOnlyRepository<City> cities;
    private final ReadOnlyRepository<Parcel> parcels;
    private final ReadOnlyRepository<Address> addresses;
    private final ReadOnlyRepository<Employee> employees;
    private final ReadOnlyRepository<Customer> customers;
    private final ReadOnlyRepository<Shipment> shipments;
    private final ReadOnlyRepository<Warehouse> warehouses;
    private final ReadOnlyRepository<WeightUnit> weightUnits;
    private final ReadOnlyRepository<ShipmentStatus> statuses;
    private final ReadOnlyRepository<ParcelCategory> categories;

    @Autowired
    public DtoMappersImpl(CityRepository cities, AddressRepository addresses,
                          CustomerRepository customers, WarehouseRepository warehouses,
                          ParcelCategoryRepository categories, WeightUnitRepository weightUnits,
                          ShipmentStatusRepository statuses, ParcelRepository parcels,
                          EmployeeRepository employees, ShipmentRepository shipments) {
        this.weightUnits = weightUnits;
        this.categories = categories;
        this.warehouses = warehouses;
        this.addresses = addresses;
        this.employees = employees;
        this.customers = customers;
        this.shipments = shipments;
        this.statuses = statuses;
        this.parcels = parcels;
        this.cities = cities;
    }

    @Override
    public Address fromDto(AddressDto info) {
        Address address = new Address();
        setAddressFields(address, info);
        return address;
    }

    @Override
    public Address fromDto(int addressId, AddressDto info) {
        Address address = get(addressId, addresses);
        setAddressFields(address, info);
        return address;
    }

    @Override
    public Parcel fromDto(ParcelDto info) {
        Parcel parcel = new Parcel();
        setParcelFields(info, parcel);
        return parcel;
    }

    @Override
    public Parcel fromDto(int parcelId, ParcelDto info) {
        Parcel parcel = get(parcelId, parcels);
        setParcelFields(info, parcel);
        return parcel;
    }

    @Override
    public Employee fromDto(EmployeeDto info) {
        Employee employee = new Employee();
        setEmployeeFields(info, employee);

        return employee;
    }

    @Override
    public Employee fromDto(int employeeId, EmployeeDto info) {
        Employee employee = get(employeeId, employees);
        setEmployeeFields(info, employee);

        return employee;
    }

    @Override
    public Customer fromDto(CustomerDto info) {
        Customer customer = new Customer();
        setCustomerFields(info, customer);
        return customer;
    }

    @Override
    public Customer fromDto(int customerId, CustomerDto info) {
        Customer customer = get(customerId, customers);
        setCustomerFields(info, customer);
        return customer;
    }

    @Override
    public Shipment fromDto(ShipmentDto info) {
        Shipment shipment = new Shipment();
        setShipmentFields(info, shipment);
        return shipment;
    }

    @Override
    public Shipment fromDto(int shipmentId, ShipmentDto info) {
        Shipment shipment = get(shipmentId, shipments);
        setShipmentFields(info, shipment);
        return shipment;
    }

    @Override
    public Warehouse warehouseFromDto(int warehouseId, AddressDto addressDto) {
        Address address = fromDto(addressDto);
        Warehouse warehouse = get(warehouseId, warehouses);
        transferAddressId(address, warehouse);
        warehouse.setAddress(address);
        return warehouse;
    }

    @Override
    public Warehouse warehouseFromDto(AddressDto addressDto) {
        Address address = fromDto(addressDto);
        return buildWarehouse(address);
    }

    private void transferAddressId(Address newAddress, Warehouse warehouse) {
        newAddress.setId(warehouse.getAddress().getId());
    }

    private void setEmployeeFields(EmployeeDto info, Employee employee) {
        setUserData(employee, info);
        employee.setPassHash(info.getPassword());
    }

    private void setCustomerFields(CustomerDto info, Customer customer) {
        setUserData(customer, info);
        customer.setDelivery(
                get(info.getAddressId(), addresses));
    }

    private void setAddressFields(Address address, AddressDto info) {
        address.setStreet1(info.getStreet1());
        address.setStreet2(info.getStreet2() != null ? info.getStreet2() : "");
        address.setPostCode(info.getPostCode());

        address.setCity(
                get(info.getCityId(), cities));
    }

    private void setShipmentFields(ShipmentDto info, Shipment shipment) {
        // format 2021-01-31
        Date date = info.getArrivalDate();
        shipment.setArrivalDate(date);

        date = info.getDepartureDate();
        shipment.setDepartureDate(date);
        shipment.setDestination(
                get(info.getWarehouseId(), warehouses));
        shipment.setStatus(
                get(info.getShipmentStatusId(), statuses));
    }

    private void setParcelFields(ParcelDto info, Parcel parcel) {
        parcel.setCustomer(
                get(info.getCustomerId(), customers));
        parcel.setDestination(
                get(info.getWarehouseId(), warehouses));
        parcel.setCategory(
                get(info.getParcelCategoryId(), categories));
        parcel.setWeightUnit(
                get(info.getWeightUnitsId(), weightUnits));
        parcel.setWeight(info.getWeight());
        if (shipmentIsInRepo(info.getShipmentId()))
            parcel.setShipmentId(info.getShipmentId());
    }

    private boolean shipmentIsInRepo(int shipmentId) {
        return get(shipmentId, shipments).getId() == shipmentId;
    }

    private void setUserData(User newUser, UserBaseDto info) {
        newUser.setFirstName(info.getFirstName());
        newUser.setLastName(info.getLastName());
        newUser.setEmail(info.getEmail());
    }

    private <E> E get(int id, ReadOnlyRepository<E> repo) {
        return repo.getById(id);
    }
}