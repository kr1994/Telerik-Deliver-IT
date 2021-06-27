package com.telerikacademy.web.deliver_it.utils;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import org.hibernate.query.Query;

import java.util.Optional;

import static com.telerikacademy.web.deliver_it.utils.Formatters.*;
import static com.telerikacademy.web.deliver_it.utils.Queries.*;

public abstract class QuerySetters {

    public static <E> void setData(Address address, Query<E> query) {
        query.setParameter(STREET_LINE_1, address.getStreet1());
        query.setParameter(STREET_LINE_2, address.getStreet2());
        query.setParameter(POST_CODE, address.getPostCode());
        setData(address.getCity(), query);
    }

    public static <E> void setData(City city, Query<E> query) {
        query.setParameter(CITY_NAME, city.getCityName());
        setData(city.getCountry(), query);
    }

    public static <E> void setData(Country country, Query<E> query) {
        query.setParameter(COUNTRY_NAME, country.getCountryName());
    }

    public static <E> void setUserData(User user, Query<E> query) {
        query.setParameter(FIRST_NAME, user.getFirstName());
        query.setParameter(LAST_NAME, user.getLastName());
        query.setParameter(EMAIL, user.getEmail());
    }

    public static <E> void setUserLike(Optional<String> firstName, Optional<String> lastName,
                                       Optional<String> email, Query<E> query) {
        query.setParameter(FIRST_NAME, firstName.orElse(""));
        query.setParameter(LAST_NAME, lastName.orElse(""));
        query.setParameter(EMAIL, email.orElse(""));
    }

    public static String buildQuery_ShipmentsGetAll(ParcelSortingDto dto) {
        String end = " and ";
        StringBuilder result = new StringBuilder(" select sh " +
                " from Shipment as sh " +
                " join sh.parcels as p " +
                " where 1=1 and ");

        if (isValidId(dto.getCustomerId()))
            result.append(" p.customer.id = :customerId ").append(end);
        if (isValidId(dto.getWarehouseId()))
            result.append(" p.destination.warehouseId = :warehouseId ").append(end);
        if (isValidWeight(dto.getWeight()) && isValid(dto.getWeightUnit())) {
            result.append(" p.weight = :weight ").append(end);
            result.append(" p.weightUnit.units = :weightUnit ").append(end);
        }
        if (isValidId(dto.getParcelCategoryId()))
            result.append(" p.category.id = :categoryId ").append(end);
        return result.substring(0, result.length() - end.length());
    }

    public static <E> void setQueryParam_ShipmentGetAll(ParcelSortingDto dto, Query<E> query) {
        if (isValidId(dto.getCustomerId()))
            query.setParameter("customerId", dto.getCustomerId());
        if (isValidId(dto.getWarehouseId()))
            query.setParameter("warehouseId", dto.getWarehouseId());
        if (isValidWeight(dto.getWeight()) && isValid(dto.getWeightUnit())) {
            query.setParameter("weight", dto.getWeight());
            query.setParameter("weightUnit", dto.getWeightUnit());
        }
        if (isValidId(dto.getParcelCategoryId()))
            query.setParameter("categoryId", dto.getParcelCategoryId());
    }

    public static String buildQuery_FilterByWeight(FilterByWeightDto dto) {
        String end = " and ";
        StringBuilder result = new StringBuilder(
                " from Parcel as p " +
                        " where 1=1 and ");
        if (isValidWeight(dto.getMinWeight()))
            result.append(" p.weight * p.weightUnit.factor >= :minWeight ").append(end);
        if (isValidWeight(dto.getMaxWeight()))
            result.append(" p.weight * p.weightUnit.factor <= :maxWeight ").append(end);

        return result.substring(0, result.length() - end.length());
    }

    public static <E> void setQueryParam_FilterByWeight(FilterByWeightDto dto,
                                                        Query<E> query) {
        if (isValidWeight(dto.getMinWeight()))
            query.setParameter("minWeight", dto.getMinWeight() * dto.getMinWeightFactor());
        if (isValidWeight(dto.getMaxWeight()))
            query.setParameter("maxWeight", dto.getMaxWeight() * dto.getMaxWeightFactor());
    }

    public static <E> void setIds(City city, Query<E> query) {
        query.setParameter(CITY_ID, city.getId());
        setIds(city.getCountry(), query);
    }

    public static <E> void setIds(Country country, Query<E> query) {
        query.setParameter(COUNTRY_ID, country.getId());
    }
}
