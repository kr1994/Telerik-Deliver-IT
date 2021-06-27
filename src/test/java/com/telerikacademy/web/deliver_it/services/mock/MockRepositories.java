package com.telerikacademy.web.deliver_it.services.mock;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;
import com.telerikacademy.web.deliver_it.repositories.contracts.*;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import org.mockito.Mockito;


import static com.telerikacademy.web.deliver_it.utils.Constants.*;

public class MockRepositories {
    private static boolean way;
    private static int idStatic;

    public static void mockAddressRepo_getById(int id, ReadOnlyRepository<Address> addresses) {
        idStatic = id;
        way = id == MOCK_WH_ADDRESS_ID;
        if (id == MOCK_ADDRESS_ID)
            whenIdThenReturnEntity(addresses, MOCK_ADDRESS);
        else
            ifElse(addresses, MOCK_WH_ADDRESS);
    }

    public static void mockCategoryRepo_getById(int id, ReadOnlyRepository<ParcelCategory> categories) {
        way = id == MOCK_PARCEL_CATEGORY_ID;
        idStatic = id;
        ifElse(categories, MOCK_CATEGORY);
    }

    public static void mockCityRepo_getById(int id, ReadOnlyRepository<City> cities) {
        way = id == WH_CITY_ID;
        idStatic = id;
        if (id == MOCK_CITY_ID)
            whenIdThenReturnEntity(cities, MOCK_CITY);
        else
            ifElse(cities, MOCK_WH_CITY);
    }

    public static void mockCustomerRepo_getById(int id, ReadOnlyRepository<Customer> customers) {
        way = id == MOCK_CUSTOMER_ID;
        idStatic = id;
        ifElse(customers, MOCK_CUSTOMER);
    }

    public static void mockEmployeeRepo_getById(int id, ReadOnlyRepository<Employee> employees) {
        way = id == MOCK_EMPLOYEE_ID;
        idStatic = id;
        ifElse(employees, MOCK_EMPLOYEE);
    }

    public static void mockParcelRepo_getById(int id, ReadOnlyRepository<Parcel> parcels) {
        way = id == MOCK_PARCEL_ID;
        idStatic = id;
        ifElse(parcels, MOCK_PARCEL);
    }

    public static void mockStatusRepo_getById(int id, ReadOnlyRepository<ShipmentStatus> statuses) {
        way = id == MOCK_SHIPMENT_STATUS_ID;
        idStatic = id;
        ifElse(statuses, MOCK_SH_STATUS);
    }

    public static void mockShipmentRepo_getById(int id, ReadOnlyRepository<Shipment> shipments) {
        way = id == MOCK_SHIPMENT_ID;
        idStatic = id;
        ifElse(shipments, MOCK_SHIPMENT);
    }

    public static void mockWarehouseRepo_getById(int id, ReadOnlyRepository<Warehouse> warehouses) {
        way = id == MOCK_WAREHOUSE_ID;
        idStatic = id;
        ifElse(warehouses, MOCK_WH);
    }

    public static void mockWeightUnitRepo_getById(int id, ReadOnlyRepository<WeightUnit> weightUnits) {
        way = id == MOCK_UNITS_ID;
        idStatic = id;
        ifElse(weightUnits, MOCK_WEIGHT_UNIT);
    }

    private static <E extends IdentifyAble> void ifElse(ReadOnlyRepository<E> repo, E entity) {
        if (way)
            whenIdThenReturnEntity(repo, entity);
        else
            throwENF(repo);
    }

    private static <E> void whenIdThenReturnEntity(ReadOnlyRepository<E> repo, E entity) {
        Mockito.when(repo.getById(idStatic))
                .thenReturn(entity);
    }

    private static <E> void throwENF(ReadOnlyRepository<E> repo) {
        Mockito.when(repo.getById(idStatic))
                .thenThrow(EntityNotFoundException.class);
    }
}