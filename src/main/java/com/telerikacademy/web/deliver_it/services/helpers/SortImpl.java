package com.telerikacademy.web.deliver_it.services.helpers;

import com.telerikacademy.web.deliver_it.models.ParcelCategory;
import com.telerikacademy.web.deliver_it.models.Shipment;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.models.dto.out.ParcelDtoOut;
import com.telerikacademy.web.deliver_it.repositories.contracts.ParcelCategoryRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentRepository;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.Sort;
import com.telerikacademy.web.deliver_it.services.helpers.contracts.DtoOutMappers;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.telerikacademy.web.deliver_it.utils.Formatters.*;

@Service
public class SortImpl implements Sort {

    private final ParcelCategoryRepository categories;
    private final ShipmentRepository shipments;
    private final DtoOutMappers mappersOut;

    @Autowired
    public SortImpl(ShipmentRepository shipments, DtoOutMappers mappersOut, ParcelCategoryRepository categories) {
        this.categories = categories;
        this.mappersOut = mappersOut;
        this.shipments = shipments;
    }

    @Override
    public List<ParcelDtoOut> getAll(ParcelSortingDto dto) {
        List<Shipment> shipmentsFromRepo = shipments.getAll(dto);
        List<ParcelDtoOut> result = getParcelDtoOuts(dto, shipmentsFromRepo);
        return sortAll(result, dto);
    }

    private List<ParcelDtoOut> sortAll(List<ParcelDtoOut> input, ParcelSortingDto dto) {
        return input.stream()
                .sorted(byArrivalDateForDto(dto.getArrivalDateSort())
                        .thenComparing(byWeightForDto(dto.getWeightSort())))
                .collect(Collectors.toList());
    }

    private Comparator<ParcelDtoOut> byWeightForDto(String weightSort) {
        if (isInvalid(weightSort))
            return (o1, o2) -> 0;
        if (weightSort.equals("ASC"))
            return Comparator.comparingDouble(o -> o.getWeight() * o.getWeightFactor());
        if (weightSort.equals("DES"))
            return (o1, o2) -> Double.compare(o2.getWeight() * o2.getWeightFactor(),
                    o1.getWeight() * o1.getWeightFactor());
        else
            throw new EntityNotFoundException("Sorting type for weight is ASC or DES");
    }

    private Comparator<ParcelDtoOut> byArrivalDateForDto(String sortingTypeByArrivalDate) {
        if (isInvalid(sortingTypeByArrivalDate))
            return (o1, o2) -> 0;
        if (sortingTypeByArrivalDate.equals("ASC"))
            return Comparator.comparing(ParcelDtoOut::getArrivalDate);
        if (sortingTypeByArrivalDate.equals("DES"))
            return (p1, p2) -> p2.getArrivalDate().compareTo(p1.getArrivalDate());
        else
            throw new EntityNotFoundException("Sorting type for arrival date is ASC or DES");
    }


    private List<ParcelDtoOut> getParcelDtoOuts(ParcelSortingDto dto, List<Shipment> shipmentsFromRepo) {
        return mappersOut.mapToParcelsDto(shipmentsFromRepo).stream()
                .filter(p -> isInvalidId(dto.getCustomerId())
                        || p.getCustomerId() == dto.getCustomerId())
                .filter(p -> isInvalidId(dto.getWarehouseId())
                        || p.getParcelWarehouseId() == dto.getWarehouseId())
                .filter(p -> isInvalidWeight(dto.getWeight())
                        || p.getWeight() == dto.getWeight())
                .filter(p -> isInvalidId(dto.getParcelCategoryId())
                        || getCategoryId(p.getCategoryName()) == dto.getParcelCategoryId())
                .collect(Collectors.toList());
    }

    private int getCategoryId(String categoryName) {
        List<ParcelCategory> cats = categories.getAll();
        return cats.stream()
                .filter(c -> c.getParcelCategory().equals(categoryName))
                .map(ParcelCategory::getId)
                .findFirst()
                .orElse(0);
    }

}
