package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.models.ShipmentStatus;
import com.telerikacademy.web.deliver_it.repositories.contracts.ShipmentStatusRepository;
import com.telerikacademy.web.deliver_it.services.contracts.ShipmentStatusService;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentStatusServiceImpl
        extends ServiceBase<ShipmentStatus>
        implements ShipmentStatusService {

    private final ShipmentStatusRepository repository;

    @Autowired
    public ShipmentStatusServiceImpl(ShipmentStatusRepository repository, Security security) {
        super(repository, security);
        this.repository = repository;
    }

    @Override
    public List<ShipmentStatus> getAll(SecurityCredentials sc) {
        if (isCustomer(sc) || isEmployee(sc))
            return repository.getAll();
        throw new UnauthorizedException();
    }
}
