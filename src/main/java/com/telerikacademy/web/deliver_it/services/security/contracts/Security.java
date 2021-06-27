package com.telerikacademy.web.deliver_it.services.security.contracts;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;

public interface Security {

    boolean isEmployee(SecurityCredentials sc);

    boolean isCustomer(SecurityCredentials sc);

    void validateEmployee(SecurityCredentials sc);

    void validateCustomer(SecurityCredentials credentials);

    void validateCustomer(int idOfCustomerToCheck, SecurityCredentials sc);

}
