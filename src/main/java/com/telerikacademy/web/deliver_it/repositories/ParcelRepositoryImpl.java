package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.FilterByWeightDto;
import com.telerikacademy.web.deliver_it.repositories.contracts.ParcelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Formatters.isValidWeight;
import static com.telerikacademy.web.deliver_it.utils.QuerySetters.*;

@Repository
public class ParcelRepositoryImpl extends RepositoryBase<Parcel> implements ParcelRepository {

    public static final String CLASS_NAME = "Parcel";
    private final SessionFactory sessionFactory;

    @Autowired
    public ParcelRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Parcel> getAll() {
        return super.getAll(Parcel.class, CLASS_NAME);
    }

    @Override
    public List<Parcel> getAllBy(int customerId, int statusId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery(
                    " select p " +
                            " from Shipment as sh " +
                            " join sh.parcels as p " +
                            " where p.customer.id = :customerId " +
                            " and sh.status.id = :statusId ", Parcel.class);
            query.setParameter("customerId", customerId);
            query.setParameter("statusId", statusId);

            return getBy(query, errMessage(customerId));
        }
    }

    @Override
    public List<Parcel> getAllBy(int statusId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery(
                    " select p " +
                            " from Shipment as sh " +
                            " join sh.parcels as p " +
                            " where sh.status.id = :statusId ", Parcel.class);
            query.setParameter("statusId", statusId);
            String errMessage = String.format("Parcels whit status id: %d not found",
                    statusId);

            return getBy(query, errMessage);
        }
    }

    @Override
    public List<Parcel> filterByWeight(FilterByWeightDto dto) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = buildQuery_FilterByWeight(dto);
            Query<Parcel> query = session.createQuery(
                    queryString, Parcel.class);
            setQueryParam_FilterByWeight(dto, query);

            String errMessage = String.format(
                    "Parcels whit weight in range min: %s - max: %s not found",
                    "" + (isValidWeight(dto.getMinWeight()) ?
                            (dto.getMinWeight() + dto.getMinUnit()) :
                            0),
                    "" + (isValidWeight(dto.getMaxWeight()) ?
                            (dto.getMaxWeight() + dto.getMaxUnit()) :
                            "max")
            );

            return getBy(query, errMessage);
        }
    }

    @Override
    public Parcel getById(int id) {
        return super.getById(Parcel.class, id, CLASS_NAME);
    }

    @Override
    public Parcel getByName(Parcel parcel) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery(
                    " from Parcel " +
                            " where customer = :customer " +
                            " and destination = :warehouse " +
                            " and category = :category " +
                            " and weight = :weight " +
                            " and weightUnit = :weightUnit ", Parcel.class);
            query.setParameter("customer", parcel.getCustomer());
            query.setParameter("warehouse", parcel.getDestination());
            query.setParameter("category", parcel.getCategory());
            query.setParameter("weight", parcel.getWeight());
            query.setParameter("weightUnit", parcel.getWeightUnit());

            return getByName(query, notFound(CLASS_NAME));
        }
    }

    private String errMessage(int customerId) {
        return String.format(
                "Parcels not found for customer whit id: %d.", customerId);
    }
}
