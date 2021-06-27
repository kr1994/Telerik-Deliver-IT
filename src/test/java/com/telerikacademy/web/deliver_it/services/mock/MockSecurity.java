package com.telerikacademy.web.deliver_it.services.mock;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.mockito.Mockito;

import static com.telerikacademy.web.deliver_it.utils.Constants.CUSTOMER_FIRST_NAME;
import static com.telerikacademy.web.deliver_it.utils.Constants.EMPLOYEE_FIRST_NAME;

public class MockSecurity {
    private static boolean way;

    public static void mockSecurity_isEmployee(SecurityCredentials sc, Security security) {
        way = sc.getName().equals(EMPLOYEE_FIRST_NAME);
        securityIfElse(security.isEmployee(sc));
    }

    public static void mockSecurity_isCustomer(SecurityCredentials sc, Security security) {
        way = sc.getName().equals(CUSTOMER_FIRST_NAME);
        securityIfElse(security.isCustomer(sc));
    }

    public static void mockSecurity_validateCustomerThrow(int userId, SecurityCredentials sc, Security security) {
        Mockito.doThrow(UnauthorizedException.class)
                .when(security).validateCustomer(userId, sc);
    }

    public static void mockSecurity_validateEmployeeThrow(SecurityCredentials sc, Security security) {
        Mockito.doThrow(UnauthorizedException.class)
                .when(security).validateEmployee(sc);
    }

    private static void securityIfElse(boolean isUser) {
        if (way)
            Mockito.when(isUser).thenReturn(true);
        else
            Mockito.when(isUser).thenReturn(false);
    }

}
