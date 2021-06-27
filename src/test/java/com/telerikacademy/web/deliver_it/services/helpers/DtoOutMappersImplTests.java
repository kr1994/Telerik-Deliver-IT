package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.models.Parcel;
import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.models.dto.out.ShipmentDtoOut;
import com.telerikacademy.web.deliver_it.repositories.contracts.ReadOnlyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.mockShipmentRepo_getById;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.mockParcelDtoOut;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.mockShipmentDtoOut;

@ExtendWith(MockitoExtension.class)
public class DtoOutMappersImplTests {

    @Mock
    ReadOnlyRepository<Shipment> shipments;

    @InjectMocks
    DtoOutMappersImpl service;

    @Test
    public void mapToParcelDto_Should_TransferCorrectly() {
        ParcelDtoOut expected = mockParcelDtoOut();

        ParcelDtoOut result = service
                .mapToParcelDto(MOCK_PARCEL, MOCK_SHIPMENT);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void mapToParcelDtoFromRepo_Should_TransferCorrectly() {
        ParcelDtoOut expected = mockParcelDtoOut();
        mockShipmentRepo_getById(MOCK_SHIPMENT_ID, shipments);

        ParcelDtoOut result = service
                .mapToDtoFromRepo(MOCK_PARCEL);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void mapToDtoFrom_Should_TransferCorrectly() {
        ParcelDtoOut expected = mockParcelDtoOut();
        String nullStr = null;
        expected.setArrivalDate(nullStr);
        expected.setShipmentStatus(nullStr);
        expected.setWarehouseCityName(nullStr);
        expected.setShipmentWarehouseId(0);

        ParcelDtoOut result = service
                .mapToDtoFrom(MOCK_PARCEL);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void mapToDto_Should_TransferCorrectly() {
        List<Parcel> input = List.of(MOCK_PARCEL, MOCK_PARCEL);
        List<ParcelDtoOut> expected = List.of(mockParcelDtoOut(), mockParcelDtoOut());

        mockShipmentRepo_getById(MOCK_SHIPMENT_ID, shipments);

        List<ParcelDtoOut> result = service.mapToDto(input);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void mapToParcelsDto_Should_TransferCorrectly() {
        List<Shipment> input = List.of(MOCK_SHIPMENT, MOCK_SHIPMENT);
        List<ParcelDtoOut> expected = List.of(mockParcelDtoOut(), mockParcelDtoOut());

        List<ParcelDtoOut> result = service.mapToParcelsDto(input);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void mapToDto_Should_TransferCorrectly2() {
        ShipmentDtoOut expected = mockShipmentDtoOut();

        ShipmentDtoOut result = service.mapToDto(MOCK_SHIPMENT);

        Assertions.assertEquals(expected, result);
    }

}
