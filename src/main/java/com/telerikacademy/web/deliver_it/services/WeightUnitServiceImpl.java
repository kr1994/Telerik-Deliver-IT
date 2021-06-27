package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.WeightUnit;
import com.telerikacademy.web.deliver_it.repositories.contracts.WeightUnitRepository;
import com.telerikacademy.web.deliver_it.services.contracts.WeightUnitService;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightUnitServiceImpl extends ServiceBase<WeightUnit> implements WeightUnitService {

    private final WeightUnitRepository repository;

    @Autowired
    public WeightUnitServiceImpl(Security security, WeightUnitRepository repository) {
        super(repository,security);
        this.repository = repository;
    }

    @Override
    public List<WeightUnit> getAll() {
        return repository.getAll();
    }
}
