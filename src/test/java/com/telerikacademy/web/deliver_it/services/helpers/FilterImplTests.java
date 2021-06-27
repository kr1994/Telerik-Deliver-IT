package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.models.Parcel;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.repositories.contracts.ParcelRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentRepository;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoOutMappers;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.mockParcelRepo_getById;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.*;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class FilterImplTests {

    @Mock
    ShipmentRepository shipments;

    @Mock
    ParcelRepository parcels;

    @Mock
    DtoOutMappers mappersOut;

    @InjectMocks
    FilterImpl service;

    @Test
    public void byStatusMapToDto_Should_CallMapper() {
        Mockito.when(parcels.getAllBy(anyInt()))
                .thenReturn(MOCK_PARCEL_LIST);

        service.byStatusMapToDto(MOCK_SHIPMENT_STATUS_ID);

        Mockito.verify(mappersOut, Mockito.times(1))
                .mapToDto(MOCK_PARCEL_LIST);
    }

    @Test
    public void customers_sOwnBy_Should_CallMapper() {
        Mockito.when(parcels.getAllBy(anyInt(), anyInt()))
                .thenReturn(MOCK_PARCEL_LIST);

        service.customers_sOwnBy(MOCK_SHIPMENT_STATUS_ID, MOCK_CUSTOMER_ID);

        Mockito.verify(mappersOut, Mockito.times(1))
                .mapToDto(MOCK_PARCEL_LIST);
    }

    @Test
    public void toDtoBy_Should_CallMapper() {
        Mockito.when(parcels.filterByWeight(NEW_FILTER_BY_WEIGHT_DTO))
                .thenReturn(MOCK_PARCEL_LIST);

        service.toDtoBy(NEW_FILTER_BY_WEIGHT_DTO);

        Mockito.verify(mappersOut, Mockito.times(1))
                .mapToDto(MOCK_PARCEL_LIST);
    }

    @Test
    public void customers_sOwnBy_Should_CallMapper2() {
        mockShipmentsGetAll();

        service.customers_sOwnBy(NEW_FILTER_BY_WEIGHT_DTO, MOCK_CUSTOMER_ID);

        Mockito.verify(mappersOut, Mockito.times(1))
                .mapToParcelsDto(SHIPMENTS_FROM_REPO);
    }

    @Test
    public void filterByWeight_Should_Filter_Whit_CustomerIdAndMinAndMax() {
        FilterByWeightDto dto = getFilterByWeightDto(MIN_WEIGHT, MAX_WEIGHT);
        mockShipmentsGetAll();
        mockMappersOutMapToParcelsDto();

        List<ParcelDtoOut> result = service.customers_sOwnBy(dto, (MOCK_CUSTOMER_ID));

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(WEIGHT_2, result.get(0).getWeight());
    }

    @Test
    public void filterByWeight_Should_Filter_Whit_CustomerIdAndMin() {
        FilterByWeightDto dto = getFilterByWeightDto(MIN_WEIGHT, null);
        mockShipmentsGetAll();
        mockMappersOutMapToParcelsDto();

        List<ParcelDtoOut> result = service.customers_sOwnBy(dto, (MOCK_CUSTOMER_ID));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(WEIGHT_2, result.get(0).getWeight());
        Assertions.assertEquals(WEIGHT_3, result.get(1).getWeight());
    }

    @Test
    public void filterByWeight_Should_Filter_Whit_CustomerIdAndMax() {
        FilterByWeightDto dto = getFilterByWeightDto(null, MAX_WEIGHT);
        mockShipmentsGetAll();
        mockMappersOutMapToParcelsDto();

        List<ParcelDtoOut> result = service.customers_sOwnBy(dto, MOCK_CUSTOMER_ID);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(WEIGHT_1, result.get(0).getWeight());
        Assertions.assertEquals(WEIGHT_2, result.get(1).getWeight());
    }

    @Test
    public void filterByWeight_Should_Filter_Whit_CustomerIdAndNoParam() {
        FilterByWeightDto dto = getFilterByWeightDto(null, null);

        mockShipmentsGetAll();
        mockMappersOutMapToParcelsDto();

        List<ParcelDtoOut> result = service.customers_sOwnBy(dto, (MOCK_CUSTOMER_ID));

        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void byCustomerAndParcel_should_CallMapper_With_CorrectInfo() {
        int parcelId = MOCK_PARCEL_ID;
        mockParcelRepo_getById(parcelId, parcels);

        service.byCustomerAndParcel(MOCK_CUSTOMER_ID, parcelId);

        Mockito.verify(mappersOut, Mockito.times(1))
                .mapToDtoFromRepo(MOCK_PARCEL);
    }

    @Test
    public void byCustomerAndParcel_Should_ThrowCorrectErr_When_ParcelNotFound() {
        int parcelId = MOCK_PARCEL_ID + 1;
        mockParcelRepo_getById(parcelId, parcels);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.byCustomerAndParcel(MOCK_CUSTOMER_ID, parcelId));
    }

    @Test
    public void getParcelIfCustomer_sOwn_Should_ReturnParcel_With_CorrectInfo() {
        mockParcelRepo_getById(MOCK_PARCEL_ID, parcels);
        Parcel result = service.getParcelIfCustomer_sOwn(MOCK_PARCEL_ID, MOCK_CUSTOMER_ID);

        Assertions.assertEquals(MOCK_PARCEL, result);
    }

    @Test
    public void getParcelIfCustomer_sOwn_Should_Throw_Whit_CustomerCredentialsAndNotMatchingParcel() {
        int customerId = MOCK_CUSTOMER_ID + 1;
        mockParcelRepo_getById(MOCK_PARCEL_ID, parcels);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.getParcelIfCustomer_sOwn(MOCK_PARCEL_ID, customerId));
    }

    @Test
    public void getParcelIfCustomer_sOwn_Should_ThrowCorrectErr_When_ParcelNotFound() {
        int wrong = MOCK_PARCEL_ID + 1;
        mockParcelRepo_getById(wrong, parcels);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.getParcelIfCustomer_sOwn(wrong, MOCK_CUSTOMER_ID));
    }

    private FilterByWeightDto getFilterByWeightDto(Double min, Double max) {
        FilterByWeightDto dto = mockFilterByWeightDto();
        dto.setMinWeight(min);
        dto.setMaxWeight(max);
        return dto;
    }

    private void mockShipmentsGetAll() {
        Mockito.when(shipments.getAll(Mockito.any(ParcelSortingDto.class)))
                .thenReturn(SHIPMENTS_FROM_REPO);
    }

    private void mockMappersOutMapToParcelsDto() {
        Mockito.when(mappersOut.mapToParcelsDto(SHIPMENTS_FROM_REPO))
                .thenReturn(PARCEL_DTO_OUTS);
    }
}
