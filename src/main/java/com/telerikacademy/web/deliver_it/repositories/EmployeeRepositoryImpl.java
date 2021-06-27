package com.telerikacademy.web.deliver_it.repositories;

import com.telerikacademy.web.deliver_it.models.Employee;
import com.telerikacademy.web.deliver_it.repositories.contracts.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.telerikacademy.web.deliver_it.utils.Queries.USER_BY_NAME_MATCHES;
import static com.telerikacademy.web.deliver_it.utils.QuerySetters.setUserData;

@Repository
public class EmployeeRepositoryImpl extends RepositoryBase<Employee> implements EmployeeRepository {

    private static final String CLASS_NAME = "Employee";
    private final SessionFactory sessionFactory;

    @Autowired
    public EmployeeRepositoryImpl(SessionFactory sessionFactory,
                                  SessionFactory sessionFactory1) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory1;
    }

    @Override
    public List<Employee> getAll() {
        return super.getAll(Employee.class, CLASS_NAME);
    }

    @Override
    public Employee getById(int id) {
        return super.getById(Employee.class, id, CLASS_NAME);
    }

    @Override
    public Employee getByName(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Query<Employee> query = session.createQuery(
                    " from Employee as u " +
                            " where " + USER_BY_NAME_MATCHES +
                            " and passHash = :passHash " +
                            " and salt = :salt ", Employee.class);
            setUserData(employee, query);
            query.setParameter("passHash", employee.getPassHash());
            query.setParameter("salt", employee.getSalt());

            String errMessage = String.format("Employee whit name %s %s and details: not found.",
                    employee.getFirstName(), employee.getLastName());

            return getByName(query, errMessage);
        }
    }

    @Override
    public String getPassHash(int employeeId) {
        return getById(employeeId).getPassHash();
    }
}
