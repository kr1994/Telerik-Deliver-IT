package com.telerikacademy.web.deliver_it.services;

import com.telerikacademy.web.deliver_it.models.Employee;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.repositories.contracts.EmployeeRepository;
import com.telerikacademy.web.deliver_it.services.contracts.EmployeeService;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.setSecurityDetails;

@Service
public class EmployeeServiceImpl extends ServiceBase<Employee> implements EmployeeService {

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employees, Security security) {
        super(employees, security);
    }

    @Override
    public Employee create(Employee employee, SecurityCredentials sc) {
        setSecurityDetails(employee);
        return super.create(employee, sc);
    }

    @Override
    public Employee update(Employee employee, SecurityCredentials sc) {
        setSecurityDetails(employee);
        return super.update(employee, sc);
    }
}
