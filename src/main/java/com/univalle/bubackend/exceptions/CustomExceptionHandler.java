package com.univalle.bubackend.exceptions;

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

}
