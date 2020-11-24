package com.droukos.cdnservice.environment.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HttpExceptionFactory {
    private HttpExceptionFactory() {
    }

    public static ResponseStatusException badRequest(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }

    public static ResponseStatusException badRequest() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public static ResponseStatusException forbidden(String msg) {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, msg);
    }

    public static ResponseStatusException forbidden() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    public static ResponseStatusException unauthorized(String msg) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, msg);
    }

    public static ResponseStatusException unauthorized() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public static ResponseStatusException badGateway(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, msg);
    }

    public static ResponseStatusException internalError() {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseStatusException internalError(String msg) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }

    public static ResponseStatusException badGateway() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY);
    }

    public static ResponseStatusException notFound(String msg) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
    }

    public static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
