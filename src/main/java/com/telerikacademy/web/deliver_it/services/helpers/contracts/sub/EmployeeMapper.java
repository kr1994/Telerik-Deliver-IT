package com.telerikacademy.web.deliver_it.services.helpers.contracts.sub;

import com.telerikacademy.web.deliver_it.models.Employee;
import com.telerikacademy.web.deliver_it.models.dto.EmployeeDto;

public interface EmployeeMapper {
    Employee fromDto(EmployeeDto info);

    Employee fromDto(int employeeId, EmployeeDto info);
}
