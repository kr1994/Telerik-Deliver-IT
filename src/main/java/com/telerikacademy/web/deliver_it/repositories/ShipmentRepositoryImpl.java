package com.telerikacademy.web.deliver_it.repositories;


import com.telerikacademy.web.deliver_it.models.*;
import com.telerikacademy.web.deliver_it.models.dto.ParcelSortingDto;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.telerikacademy.web.deliver_it.utils.QuerySetters.*;

@Repository
public class ShipmentRepositoryImpl extends RepositoryBase<Shipment> implements ShipmentRepository {

    public static final String CLASS_NAME = "Shipment";
    private final SessionFactory sessionFactory;

    @Autowired
    public ShipmentRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Shipment> getAll() {
        return super.getAll(Shipment.class, CLASS_NAME);
    }

    @Override
    public List<Shipment> getByWarehouse(int warehouseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    " from Shipment as sh " +
                            " where sh.destination.id = :warehouseId", Shipment.class);
            query.setParameter("warehouseId", warehouseId);

            return getBy(query, getMessage(warehouseId, "warehouse"));
        }
    }

    @Override
    public List<Shipment> getByCustomer(int customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    " select sh " +
                            " from Shipment as sh " +
                            " join sh.parcels as p " +
                            " where p.customer.userId = :customerId ", Shipment.class);
            query.setParameter("customerId", customerId);

            return getBy(query, getMessage(customerId, "customer"));
        }
    }

    @Override
    public List<Shipment> getByStatus(int warehouseId, Optional<Integer> statusId) {
        if (statusId.isEmpty())
            return getByWarehouse(warehouseId);

        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    " from Shipment as sh " +
                            " where sh.destination.id = :warehouseId " +
                            " and sh.status.id = :statusId ", Shipment.class);
            query.setParameter("warehouseId", warehouseId);
            query.setParameter("statusId", statusId.get());

            String errMessage = String.format(
                    "Shipments for warehouse whit id: %d, and shipment status id: %d - not found.",
                    warehouseId, statusId.get());
            return getBy(query, errMessage);
        }
    }

    @Override
    public List<Shipment> getAll(ParcelSortingDto dto) {
        String queryString = buildQuery_ShipmentsGetAll(dto);

        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    queryString, Shipment.class);

            setQueryParam_ShipmentGetAll(dto, query);

            List<Shipment> withDuplications = getBy(query, notFound("Parcels"));
            Set<Shipment> set = new HashSet<>(withDuplications);
            return new ArrayList<>(set);
        }
    }

    @Override
    public Shipment getById(int id) {
        return super.getById(Shipment.class, id, CLASS_NAME);
    }

    @Override
    public Shipment getByName(Shipment shipment) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    " from Shipment as sh " +
                            " where sh.departureDate = :dDate " +
                            " and sh.arrivalDate = :aDate " +
                            " and sh.destination = :destination " +
                            " and sh.status = :status ", Shipment.class);
            query.setParameter("dDate", shipment.getDepartureDate());
            query.setParameter("aDate", shipment.getArrivalDate());
            query.setParameter("destination", shipment.getDestination());
            query.setParameter("status", shipment.getStatus());

            return getByName(query, notFound(CLASS_NAME));
        }
    }

    @Override
    public Shipment getNext(Warehouse warehouse, Date currentDate, ShipmentStatus status) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    " from Shipment as sh " +
                            " where sh.departureDate > :currentDate " +
                            " and sh.destination = :warehouse " +
                            " and sh.status.id < :statusId " +
                            " order by departureDate asc ", Shipment.class);
            query.setParameter("currentDate", currentDate);
            query.setParameter("warehouse", warehouse);
            query.setParameter("statusId", status.getId());

            return getByName(query, notFound(CLASS_NAME));
        }
    }

    private String getMessage(int warehouseId, String entity) {
        return String.format(
                "Shipments not found for %s whit id %d.", entity, warehouseId);
    }
}
