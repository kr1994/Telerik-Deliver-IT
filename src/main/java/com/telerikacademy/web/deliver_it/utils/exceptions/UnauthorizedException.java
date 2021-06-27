package com.telerikacademy.web.deliver_it.utils.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Authorisation failed");
    }
}

