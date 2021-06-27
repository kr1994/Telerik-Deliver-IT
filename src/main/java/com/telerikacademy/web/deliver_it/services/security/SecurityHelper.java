package com.telerikacademy.web.deliver_it.services.security;


import com.telerikacademy.web.deliver_it.models.Customer;
import com.telerikacademy.web.deliver_it.models.Employee;
import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

import static com.telerikacademy.web.deliver_it.utils.Templates.*;

public abstract class SecurityHelper {

    public static SecurityCredentials buildCredential(HttpHeaders header) {
        if (header.isEmpty() || !header.containsKey(HEADER_KEY_NAME))
            throw new UnauthorizedException();
        SecurityCredentials sc = new SecurityCredentials();

        sc.setId(getInt(header, HEADER_KEY_ID));
        sc.setName(getString(header, HEADER_KEY_NAME));
        sc.setPassword(getString(header, HEADER_KEY_PASS));

        return sc;
    }

    public static SecurityCredentials buildCredential(Customer customer) {
        SecurityCredentials sc = new SecurityCredentials();
        sc.setId(customer.getId());
        sc.setName(customer.getFirstName());
        return sc;
    }

    public static void setSecurityDetails(Employee employee) {
        setSalt(employee);
        employee.setPassHash(
                generatePassHash(employee.getPassHash(), employee.getSalt()));
    }

    static String generatePassHash(String password, double salt) {
        return "" + (password + salt).hashCode();
    }

    private static int getInt(HttpHeaders header, String key) {
        if (!header.containsKey(key))
            throw new UnauthorizedException();

        return Integer.parseInt(Objects.requireNonNull(header.getFirst(key)));
    }

    private static String getString(HttpHeaders header, String key) {
        String result = header.getFirst(key);
        return result == null ? "" : result;
    }

    private static void setSalt(Employee employee) {
        employee.setSalt(Math.random());
    }

}
