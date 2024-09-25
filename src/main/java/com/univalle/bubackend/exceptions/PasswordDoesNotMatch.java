package com.univalle.bubackend.exceptions;

public class PasswordDoesNotMatch extends RuntimeException {
    public PasswordDoesNotMatch(String message) {
        super(message);
    }
}
