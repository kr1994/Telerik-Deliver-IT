package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentRepository;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoOutMappers;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.SORTING_TYPE_ASCENDING;
import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.SORTING_TYPE_DESCENDING;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;

@ExtendWith(MockitoExtension.class)
public class SortImplTests {

    @Mock
    ShipmentRepository shipments;

    @Mock
    DtoOutMappers mappersOut;

    @InjectMocks
    SortImpl service;

    @Test
    public void getAll_Should_SortByWeightDES() {
        ParcelSortingDto dto = getParcelSortingDtoWithWeightSort(SORTING_TYPE_DESCENDING);
        mockShipmentsGetAll(dto, UNSORTED_BY_WEIGHT_SHIPMENTS);
        mockMappersOutMapToParcelsDto(UNSORTED_BY_WEIGHT_SHIPMENTS, UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT);

        List<ParcelDtoOut> result = service.getAll(dto);

        Assertions.assertTrue(result.get(0).getWeight() >= result.get(1).getWeight());
        Assertions.assertTrue(result.get(1).getWeight() >= result.get(2).getWeight());
    }

    @Test
    public void getAll_Should_SortByWeightASC() {
        ParcelSortingDto dto = getParcelSortingDtoWithWeightSort(SORTING_TYPE_ASCENDING);
        mockShipmentsGetAll(dto, UNSORTED_BY_WEIGHT_SHIPMENTS);
        mockMappersOutMapToParcelsDto(UNSORTED_BY_WEIGHT_SHIPMENTS, UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT);

        List<ParcelDtoOut> result = service.getAll(dto);

        Assertions.assertTrue(result.get(0).getWeight() <= result.get(1).getWeight());
        Assertions.assertTrue(result.get(1).getWeight() <= result.get(2).getWeight());
    }

    @Test
    public void getAll_Should_SortByWeightDontChangeIfEmpty() {
        mockShipmentsGetAll(NEW_PARCEL_SORTING_DTO, UNSORTED_BY_WEIGHT_SHIPMENTS);
        mockMappersOutMapToParcelsDto(UNSORTED_BY_WEIGHT_SHIPMENTS, UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT);
        List<ParcelDtoOut> result = service.getAll(NEW_PARCEL_SORTING_DTO);

        Assertions.assertEquals(UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT, result);
    }

    @Test
    public void getAll_Should_Throw_When_WrongSortingTypeByWeight() {
        ParcelSortingDto dto = getParcelSortingDtoWithWeightSort(WRONG);
        mockShipmentsGetAll(dto, NEW_SHIPMENT_LIST);
        mockMappersOutMapToParcelsDto(NEW_SHIPMENT_LIST, NEW_PARCEL_DTO_OUT_LIST);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.getAll(dto));
    }

    @Test
    public void getAll_Should_SortByArrivalDateDES() {
        ParcelSortingDto dto = getParcelSortingDtoWithArrivalDateSort(SORTING_TYPE_DESCENDING);
        mockShipmentsGetAll(dto, UNSORTED_BY_WEIGHT_SHIPMENTS);
        mockMappersOutMapToParcelsDto(UNSORTED_BY_WEIGHT_SHIPMENTS, UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT);

        List<ParcelDtoOut> result = service.getAll(dto);

        Assertions.assertEquals(DATE_3, result.get(0).getArrivalDate());
        Assertions.assertEquals(DATE_2, result.get(1).getArrivalDate());
        Assertions.assertEquals(DATE_1, result.get(2).getArrivalDate());
    }

    @Test
    public void getAll_Should_SortByArrivalDateASC() {
        ParcelSortingDto dto = getParcelSortingDtoWithArrivalDateSort(SORTING_TYPE_ASCENDING);
        mockShipmentsGetAll(dto, UNSORTED_BY_WEIGHT_SHIPMENTS);
        mockMappersOutMapToParcelsDto(UNSORTED_BY_WEIGHT_SHIPMENTS, UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT);

        List<ParcelDtoOut> result = service.getAll(dto);

        Assertions.assertEquals(DATE_1, result.get(0).getArrivalDate());
        Assertions.assertEquals(DATE_2, result.get(1).getArrivalDate());
        Assertions.assertEquals(DATE_3, result.get(2).getArrivalDate());
    }

    @Test
    public void getAll_ShouldNotChangeOrder_With_Empty() {
        mockShipmentsGetAll(NEW_PARCEL_SORTING_DTO, UNSORTED_BY_WEIGHT_SHIPMENTS);
        mockMappersOutMapToParcelsDto(UNSORTED_BY_WEIGHT_SHIPMENTS, UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT);

        List<ParcelDtoOut> result = service.getAll(NEW_PARCEL_SORTING_DTO);

        Assertions.assertEquals(UNSORTED_BY_WEIGHT_PARCELS_DTO_OUT, result);
    }

    @Test
    public void getAll_Should_Throw_When_WrongSortingTypeByDate() {
        ParcelSortingDto dto = getParcelSortingDtoWithArrivalDateSort(WRONG);

        mockShipmentsGetAll(dto, NEW_SHIPMENT_LIST);
        mockMappersOutMapToParcelsDto(NEW_SHIPMENT_LIST, NEW_PARCEL_DTO_OUT_LIST);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.getAll(dto));
    }

    private void mockMappersOutMapToParcelsDto(List<Shipment> fromRepo, List<ParcelDtoOut> fromMapper) {
        Mockito.when(mappersOut.mapToParcelsDto(fromRepo))
                .thenReturn(fromMapper);
    }

    private void mockShipmentsGetAll(ParcelSortingDto dto, List<Shipment> fromRepo) {
        Mockito.when(shipments.getAll(dto))
                .thenReturn(fromRepo);
    }

    private ParcelSortingDto getParcelSortingDtoWithArrivalDateSort(String sortingType) {
        ParcelSortingDto dto = new ParcelSortingDto();
        dto.setArrivalDateSort(sortingType);
        return dto;
    }

    private ParcelSortingDto getParcelSortingDtoWithWeightSort(String sortingType) {
        ParcelSortingDto dto = new ParcelSortingDto();
        dto.setWeightSort(sortingType);
        return dto;
    }
}
