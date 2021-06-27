package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.ParcelCategory;
import com.telerikacademy.web.deliver_it.repositories.contracts.ParcelCategoryRepository;
import com.telerikacademy.web.deliver_it.services.contracts.ParcelCategoryService;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParcelCategoryServiceImpl extends ServiceBase<ParcelCategory> implements ParcelCategoryService {

    private final ParcelCategoryRepository repository;

    @Autowired
    public ParcelCategoryServiceImpl(Security security, ParcelCategoryRepository repository) {
        super(repository, security);
        this.repository = repository;
    }

    @Override
    public List<ParcelCategory> getAll() {
        return repository.getAll();
    }
}
