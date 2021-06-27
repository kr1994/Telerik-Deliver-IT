package com.telerikacademy.web.deliver_it.controllers.mvc;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.services.security.contracts.Security;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;

import javax.servlet.http.HttpSession;

import static com.telerikacademy.web.deliver_it.controllers.mvc.Constants.CREDENTIALS;

public abstract class MvcHelpers {

    public static SecurityCredentials getCredential(HttpSession session) {
        SecurityCredentials sc = (SecurityCredentials) session.getAttribute(CREDENTIALS);
        if(sc == null)
            throw new UnauthorizedException();
        return sc;
    }

    public static void validateEmployee(HttpSession session, Security security) {
        SecurityCredentials sc = getCredential(session);
        security.validateEmployee(sc);
    }

    public static SecurityCredentials getValidatedEmployeeCredential(HttpSession session, Security security) {
        SecurityCredentials sc = getCredential(session);
        security.validateEmployee(sc);
        return sc;
    }

    public static SecurityCredentials getValidatedCustomerCredential(HttpSession session, Security security) {
        SecurityCredentials sc = getCredential(session);
        security.validateCustomer(sc);
        return sc;
    }

    public static SecurityCredentials getValidatedCredential(HttpSession session, Security security) {
        SecurityCredentials sc = getCredential(session);
        if (security.isEmployee(sc) || security.isCustomer(sc))
            return sc;
        throw new UnauthorizedException();
    }

}
