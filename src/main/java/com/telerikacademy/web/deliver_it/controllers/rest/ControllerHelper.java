package com.telerikacademy.web.deliver_it.controllers.rest;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;
import com.telerikacademy.web.deliver_it.services.contracts.sub.*;
import com.telerikacademy.web.deliver_it.utils.exceptions.DuplicateEntityException;
import com.telerikacademy.web.deliver_it.utils.exceptions.EntityNotFoundException;
import com.telerikacademy.web.deliver_it.utils.exceptions.UnauthorizedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.telerikacademy.web.deliver_it.services.security.SecurityHelper.buildCredential;

public abstract class ControllerHelper {

    public static <E> E delete(int id, HttpHeaders header, DeleteService<E> service) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.delete(id, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public static <E> E getById(int id, HttpHeaders header, GetByIdService<E> service) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.getById(id, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public static <E> E getById(int id, GetById<E> service) {
        try {
            return service.getById(id);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public static <E> E update(E entity, HttpHeaders headers, UpdateService<E> service) {
        try {
            SecurityCredentials sc = getCredential(headers);
            return service.update(entity, sc);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public static <E> E create(E entity, Create<E> service) {
        try {
            return service.create(entity);

        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public static <E> E create(E entity, HttpHeaders header, CreateService<E> service) {
        try {
            SecurityCredentials sc = getCredential(header);
            return service.create(entity, sc);

        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static SecurityCredentials getCredential(HttpHeaders header) {
        return buildCredential(header);
    }

}
