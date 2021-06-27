package com.telerikacademy.web.deliver_it.services.security;


import com.telerikacademy.web.deliver_it.models.Employee;
import com.telerikacademy.web.deliver_it.repositories.contracts.CustomerRepository;
import com.telerikacademy.web.deliver_it.repositories.contracts.EmployeeRepository;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.buildCredential;
import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.setSecurityDetails;
import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.mockCustomerRepo_getById;
import static com.telerikacademy.web.deliver_it.services.mock.MockRepositories.mockEmployeeRepo_getById;
import static com.telerikacademy.web.deliver_it.utils.Constants.*;
import static com.telerikacademy.web.deliver_it.utils.MockBuilders.*;
import static com.telerikacademy.web.deliver_it.utils.Templates.*;

@ExtendWith(MockitoExtension.class)
public class SecurityImplTests {

    @Mock
    CustomerRepository customers;

    @Mock
    EmployeeRepository employees;

    @InjectMocks
    SecurityImpl security;

    @Test
    public void buildCredential_Should_ReturnExpectedAnswer_ForCustomer() {
        SecurityCredentials expected = SC_CUSTOMER;
        SecurityCredentials sc = buildCredential(MOCK_CUSTOMER_HEADERS);

        Assertions.assertEquals(expected.getId(), sc.getId());
        Assertions.assertEquals(expected.getName(), sc.getName());
        Assertions.assertEquals("", sc.getPassword());
    }

    @Test
    public void buildCredential_Should_ThrowWhitEmptyHeaders() {
        HttpHeaders emptyHeaders = new HttpHeaders();

        Assertions.assertThrows(UnauthorizedException.class,
                () -> buildCredential(emptyHeaders));
    }

    @Test
    public void buildCredential_Should_ThrowIfIdIsMissing() {
        HttpHeaders wrong = new HttpHeaders();
        wrong.add(HEADER_KEY_NAME, "wrong");
        wrong.add(HEADER_KEY_PASS, "wrong");

        Assertions.assertThrows(UnauthorizedException.class,
                () -> buildCredential(wrong));
    }

    @Test
    public void buildCredential_Should_ThrowIfNameIsMissing() {
        HttpHeaders wrong = new HttpHeaders();
        wrong.add(HEADER_KEY_ID, "wrong");
        wrong.add(HEADER_KEY_PASS, "wrong");

        Assertions.assertThrows(UnauthorizedException.class,
                () -> buildCredential(wrong));
    }

    @Test
    public void buildCredential_Should_ReturnExpectedAnswer_ForEmployee() {
        SecurityCredentials sc = buildCredential(MOCK_EMPLOYEE_HEADERS);

        Assertions.assertEquals(SC_EMPLOYEE, sc);
    }

    @Test
    public void isCustomer_Should_ReturnTrue_Whit_CorrectInput() {
        mockCustomerRepo_getById(SC_CUSTOMER.getId(), customers);

        Assertions.assertTrue(security.isCustomer(SC_CUSTOMER));
    }

    @Test
    public void isCustomer_Should_ReturnFalse_When_InvalidCustomerId() {
        SecurityCredentials SC_wrongId = mockSecurityCredentialCustomer();
        SC_wrongId.setId(SC_wrongId.getId() + 1);
        mockCustomerRepo_getById(SC_wrongId.getId(), customers);

        Assertions.assertFalse(security.isCustomer(SC_wrongId));
    }

    @Test
    public void isCustomer_Should_ReturnFalse_When_NamesNotMatch() {
        SecurityCredentials SC_wrongName = mockSecurityCredentialCustomer();
        SC_wrongName.setName("wrong");
        mockCustomerRepo_getById(SC_wrongName.getId(), customers);

        Assertions.assertFalse(security.isCustomer(SC_wrongName));
    }

    @Test
    public void validateCustomerWhitID_Should_Pass_Whit_CorrectInput() {
        SecurityCredentials validSC = SC_CUSTOMER;
        mockCustomerRepo_getById(validSC.getId(), customers);

        Assertions.assertDoesNotThrow(
                () -> security.validateCustomer(validSC.getId(), validSC));
    }

    @Test
    public void validateCustomerWhitID_Should_Throw_Whit_WrongName() {
        SecurityCredentials SC_wrongName = mockSecurityCredentialCustomer();
        SC_wrongName.setName("wrong");
        mockCustomerRepo_getById(SC_wrongName.getId(), customers);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> security.validateCustomer(SC_wrongName.getId(), SC_wrongName));
    }

    @Test
    public void validateCustomerWhitID_Should_Throw_Whit_WrongId() {
        SecurityCredentials validSC = mockSecurityCredentialCustomer();
        int wrong = validSC.getId() + 1;

        Assertions.assertThrows(UnauthorizedException.class,
                () -> security.validateCustomer(wrong, validSC));
    }

    @Test
    public void isEmployee_Should_ReturnTrue_Whit_CorrectInput() {
        SecurityCredentials validSC = SC_EMPLOYEE;
        mockEmployeeRepo_getById(validSC.getId(), employees);

        Assertions.assertTrue(security.isEmployee(validSC));
    }

    @Test
    public void isEmployee_Should_ReturnFalse_Whit_InvalidName() {
        SecurityCredentials SC_wrongName = mockSecurityCredentialEmployee();
        SC_wrongName.setName("wrong");
        mockEmployeeRepo_getById(SC_wrongName.getId(), employees);

        Assertions.assertFalse(security.isEmployee(SC_wrongName));
    }

    @Test
    public void isEmployee_Should_ReturnFalse_Whit_InvalidPass() {
        SecurityCredentials SC_wrongPass = mockSecurityCredentialEmployee();
        SC_wrongPass.setPassword("wrong");
        mockEmployeeRepo_getById(SC_wrongPass.getId(), employees);

        Assertions.assertFalse(security.isEmployee(SC_wrongPass));
    }

    @Test
    public void isEmployee_Should_ReturnFalse_Whit_InvalidId() {
        SecurityCredentials SC_wrongId = mockSecurityCredentialEmployee();
        SC_wrongId.setId(0);
        mockEmployeeRepo_getById(SC_wrongId.getId(), employees);

        Assertions.assertFalse(security.isEmployee(SC_wrongId));
    }

    @Test
    public void setSecurityDetails_Should_SetCorrectData() {
        Employee employee = mockEmployeeFromUserInput();
        SecurityCredentials sc = SC_EMPLOYEE;
        mockEmployeeRepo_getById(sc.getId(), employees);

        setSecurityDetails(employee);

        Assertions.assertTrue(security.isEmployee(sc));
    }
}
