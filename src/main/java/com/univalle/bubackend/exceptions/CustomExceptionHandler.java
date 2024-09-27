package com.univalle.bubackend.exceptions;

import com.univalle.bubackend.exceptions.resetpassword.AlreadyLinkHasBeenCreated;
import com.univalle.bubackend.exceptions.resetpassword.PasswordDoesNotMatch;
import com.univalle.bubackend.exceptions.resetpassword.TokenExpired;
import com.univalle.bubackend.exceptions.resetpassword.TokenNotFound;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleSQLIntegrityConstraintViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Ya existe un usuario con ese username";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDTO(errorMessage) );
    }

}
