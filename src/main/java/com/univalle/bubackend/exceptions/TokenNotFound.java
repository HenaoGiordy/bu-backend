package com.univalle.bubackend.exceptions;

public class TokenNotFound extends RuntimeException {
    public TokenNotFound(String tokenNoEncontrado) {
        super(tokenNoEncontrado);
    }
}
