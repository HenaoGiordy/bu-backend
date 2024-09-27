package com.univalle.bubackend.exceptions;

import com.univalle.bubackend.exceptions.resetpassword.AlreadyLinkHasBeenCreated;
import com.univalle.bubackend.exceptions.resetpassword.PasswordDoesNotMatch;
import com.univalle.bubackend.exceptions.resetpassword.TokenExpired;
import com.univalle.bubackend.exceptions.resetpassword.TokenNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(AlreadyLinkHasBeenCreated.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleAlreadyLinkHasBeenCreated(AlreadyLinkHasBeenCreated ex) {

        Map<String, String> map = new HashMap<>();
        map.put("Error", ex.getMessage());

        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordDoesNotMatch.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handlePasswordDoesNotMatch(PasswordDoesNotMatch ex) {
        Map<String, String> map = new HashMap<>();
        map.put("Error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpired.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handleTokenExpired(TokenExpired ex) {
        Map<String, String> map = new HashMap<>();
        map.put("Error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleTokenNotFound(TokenNotFound ex) {
        Map<String, String> map = new HashMap<>();
        map.put("Error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

}
