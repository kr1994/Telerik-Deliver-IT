package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.ShipmentStatus;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentStatusRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ShipmentStatusRepositoryImpl
        extends RepositoryBase<ShipmentStatus>
        implements ShipmentStatusRepository {

    public static final String CLASS_NAME = "ShipmentStatus";
    private final SessionFactory sessionFactory;

    @Autowired
    public ShipmentStatusRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ShipmentStatus> getAll() {
        return super.getAll(ShipmentStatus.class, CLASS_NAME);
    }

    @Override
    public ShipmentStatus getById(int id) {
        return super.getById(ShipmentStatus.class, id, CLASS_NAME);
    }

    @Override
    public ShipmentStatus getByName(ShipmentStatus status) {
        try (Session session = sessionFactory.openSession()) {
            Query<ShipmentStatus> query = session.createQuery(
                    " from ShipmentStatus " +
                            " where status = :status ", ShipmentStatus.class);
                query.setParameter("status",status.getStatus());

            return getByName(query, getErrMessageBy("Shipment status", status.getStatus()));
        }
    }
}
