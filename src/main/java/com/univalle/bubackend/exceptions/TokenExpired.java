package com.univalle.bubackend.exceptions;

public class TokenExpired extends RuntimeException {
    public TokenExpired(String s) {
        super(s);
    }
}
