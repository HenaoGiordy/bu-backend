package com.univalle.bubackend.exceptions;

public class RoleNotFound extends RuntimeException {
    public RoleNotFound(String s) {
        super(s);
    }
}