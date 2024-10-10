package com.univalle.bubackend.exceptions;

public class InvalidDateFormat extends RuntimeException {
    public InvalidDateFormat(String s) {
        super(s);
    }
}
