package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.*;
import com.telerikacademy.web.deliver_it.services.helpers.DtoMappersImpl;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.repositories.contracts.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.*;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.*;
import static com.telerikacademy.web.deliver_it.utils.MockDto.*;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class DtoMappersImplTests {

    @Mock
    AddressRepository addresses;

    @Mock
    WarehouseRepository warehouses;

    @Mock
    CustomerRepository customers;

    @Mock
    EmployeeRepository employees;

    @Mock
    ParcelCategoryRepository categories;

    @Mock
    WeightUnitRepository weightUnits;

    @Mock
    CityRepository cities;

    @Mock
    ParcelRepository parcels;

    @Mock
    ShipmentStatusRepository statuses;

    @Mock
    ShipmentRepository shipments;

    @InjectMocks
    DtoMappersImpl service;

    @Test
    public void addressFromDto_Should_TransferCorrectly() {
        AddressDto dto = buildCustomerAddressDto();
        mockCityRepo_getById(dto.getCityId(), cities);

        Address address = service.fromDto(dto);

        Assertions.assertEquals(dto.getStreet1(), address.getStreet1());
        Assertions.assertEquals(dto.getStreet2(), address.getStreet2());
        Assertions.assertEquals(dto.getPostCode(), address.getPostCode());
        Assertions.assertEquals(dto.getCityId(), address.getCity().getId());
    }

    @Test
    public void addressFromDto_Should_TransferCorrectly_WhitNullAsStr2() {
        AddressDto dto = buildCustomerAddressDto();
        dto.setStreet2(null);
        mockCityRepo_getById(dto.getCityId(), cities);

        Address address = service.fromDto(dto);

        Assertions.assertEquals(dto.getStreet1(), address.getStreet1());
        Assertions.assertEquals("", address.getStreet2());
        Assertions.assertEquals(dto.getPostCode(), address.getPostCode());
        Assertions.assertEquals(dto.getCityId(), address.getCity().getId());
    }

    @Test
    public void addressFromDto_Should_Throw_WhitWrongCitiId() {
        mockCityRepo_getById(anyInt(), cities);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.fromDto(new AddressDto()));
    }

    @Test
    public void addressFromDtoWhitId_Should_TransferCorrectly() {
        AddressDto dto = buildCustomerAddressDto();
        mockAddressRepo_getById(MOCK_ADDRESS_ID, addresses);
        mockCityRepo_getById(MOCK_CITY_ID, cities);

        Address result = service.fromDto(MOCK_ADDRESS_ID, dto);

        Assertions.assertEquals(MOCK_ADDRESS, result);
        Assertions.assertEquals(MOCK_ADDRESS.getId(), result.getId());
    }

    @Test
    public void parcelFromDto_Should_TransferCorrectly() {
        ParcelDto dto = buildParcelDto();
        Parcel expected = mockParcel();
        mockReposFor(dto);

        Parcel result = service.fromDto(dto);

        //can differ by id
        Assertions.assertNotEquals(expected.getId(), result.getId());

        Assertions.assertEquals(expected.getCategory(), result.getCategory());
        Assertions.assertEquals(expected.getCustomer(), result.getCustomer());
        Assertions.assertEquals(expected.getDestination(), result.getDestination());
        Assertions.assertEquals(expected.getWeight(), result.getWeight());
        Assertions.assertEquals(expected.getWeightUnit(), result.getWeightUnit());
        Assertions.assertEquals(expected.getShipmentId(), result.getShipmentId());
    }

    @Test
    public void parcelFromDtoWhitId_Should_TransferCorrectly() {
        ParcelDto dto = buildParcelDto();
        int id = MOCK_PARCEL_ID;
        Parcel stock = new Parcel();
        stock.setId(id);

        mockReposFor(dto);
        Mockito.when(parcels.getById(id))
                .thenReturn(stock);

        Parcel result = service.fromDto(id, dto);

        Assertions.assertEquals(MOCK_PARCEL, result);
    }

    private void mockReposFor(ParcelDto dto) {
        mockWarehouseRepo_getById(dto.getWarehouseId(), warehouses);
        mockCustomerRepo_getById(dto.getCustomerId(), customers);
        mockCategoryRepo_getById(dto.getParcelCategoryId(), categories);
        mockWeightUnitRepo_getById(dto.getWeightUnitsId(), weightUnits);
        mockShipmentRepo_getById(dto.getShipmentId(), shipments);
    }

    @Test
    public void customerFromDto_Should_TransferCorrectly() {
        CustomerDto dto = buildCustomerDto();
        mockAddressRepo_getById(dto.getAddressId(), addresses);

        Customer result = service.fromDto(dto);

        Assertions.assertEquals(MOCK_CUSTOMER, result);
    }

    @Test
    public void customerFromDtoWhitId_Should_TransferCorrectly() {
        CustomerDto dto = buildCustomerDto();
        int id = 9;
        Customer stock = new Customer();
        stock.setId(id);
        Customer expected = mockCustomer();
        expected.setId(id);

        mockAddressRepo_getById(dto.getAddressId(), addresses);
        Mockito.when(customers.getById(id))
                .thenReturn(stock);

        Customer result = service.fromDto(id, dto);

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected.getId(), result.getId());
    }

    @Test
    public void shipmentFromDto_Should_TransferCorrectly() {
        ShipmentDto dto = buildShipmentDto();
        mockWarehouseRepo_getById(dto.getWarehouseId(), warehouses);
        mockStatusRepo_getById(dto.getShipmentStatusId(), statuses);

        Shipment result = service.fromDto(dto);

        Assertions.assertEquals(MOCK_SHIPMENT, result);
    }

    @Test
    public void shipmentFromDtoWhitId_Should_TransferCorrectly() {
        ShipmentDto dto = buildShipmentDto();
        int id = 13;
        Shipment stock = new Shipment();
        stock.setId(id);
        Shipment expected = mockShipment();
        expected.setId(id);

        mockWarehouseRepo_getById(dto.getWarehouseId(), warehouses);
        mockStatusRepo_getById(dto.getShipmentStatusId(), statuses);
        Mockito.when(shipments.getById(id))
                .thenReturn(stock);

        Shipment result = service.fromDto(id, dto);

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected.getId(), result.getId());
    }

    @Test
    public void warehouseFromDto_Should_TransferCorrectly() {
        AddressDto dto = buildWarehouseAddressDto();
        Warehouse stock = buildBlankWarehouse();
        Warehouse expected = MOCK_WH;

        mockCityRepo_getById(WH_CITY_ID, cities);
        Mockito.when(warehouses.getById(MOCK_WAREHOUSE_ID))
                .thenReturn(stock);

        Warehouse result = service.warehouseFromDto(MOCK_WAREHOUSE_ID, dto);

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getAddress().getId(), result.getAddress().getId());
    }

    @Test
    public void warehouseFromDto_Should_TransferCorrectly_Whit_AddressOnly() {
        AddressDto dto = buildWarehouseAddressDto();
        mockCityRepo_getById(WH_CITY_ID, cities);

        Warehouse result = service.warehouseFromDto(dto);

        Assertions.assertEquals(MOCK_WH, result);
    }

    private Warehouse buildBlankWarehouse() {
        Warehouse stock = mockWarehouse();
        stock.getAddress().setPostCode("");
        stock.getAddress().setStreet1("");
        stock.getAddress().setStreet2("");
        stock.getAddress().setCity(new City());
        return stock;
    }

    @Test
    public void EmployeeFromDto_should_TransferCorrectly() {
        EmployeeDto dto = buildEmployeeDto();
        Employee expected = mockEmployeeFromUserInput();

        Employee result = service.fromDto(dto);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void EmployeeFromDtoWhitId_should_TransferCorrectly() {
        EmployeeDto dto = buildEmployeeDto();
        int id = 54;
        Employee stock = new Employee();
        stock.setId(id);
        Employee expected = mockEmployeeFromUserInput();
        expected.setId(id);

        Mockito.when(employees.getById(id))
                .thenReturn(stock);

        Employee result = service.fromDto(id, dto);

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected.getId(), result.getId());
    }
}
