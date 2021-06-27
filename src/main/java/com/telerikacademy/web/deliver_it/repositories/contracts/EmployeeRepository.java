package com.telerikacademy.web.deliver_it.repositories.contracts;

import com.telerikacademy.web.deliver_it.models.Employee;

public interface EmployeeRepository extends Repository<Employee> {

    String getPassHash(int employeeId);
}
