package com.univalle.bubackend.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.univalle.bubackend.exceptions.appointment.HasNoAvailableDates;
import com.univalle.bubackend.exceptions.appointment.NotProfessional;
import com.univalle.bubackend.exceptions.appointment.NotValidTypeAppointment;
import com.univalle.bubackend.exceptions.change_password.PasswordError;
import com.univalle.bubackend.exceptions.change_password.UserNotFound;
import com.univalle.bubackend.exceptions.report.BecaInvalid;
import com.univalle.bubackend.exceptions.report.ReportNotFound;
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
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    public ResponseEntity<ExceptionDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Error de integridad de datos.";

        // Obtener el mensaje de error
        String message = Objects.requireNonNull(ex.getRootCause()).getMessage();

        //No hayan dos citas en la misma hora
        if (message.contains("available_dates_date_time_professional_id_key")) {
            errorMessage = "El profesional ya tiene una cita asignada en esa fecha y hora.";
        }
        //Para el username del UserEntity
        if (message.contains("user_entity_username_key")) {
            errorMessage = "Ya existe un usuario con ese nombre de usuario.";
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDTO(errorMessage));
    }

    @ExceptionHandler(RoleNotFound.class)
    public ResponseEntity<ExceptionDTO> roleNotFoundException(RoleNotFound ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(errorMessage) );
    }


    @ExceptionHandler(CSVFieldException.class)
    public ResponseEntity<Map<String, String>> handleCSVFieldException(CSVFieldException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Map<String, String>> handleJWTVerificationException(JWTVerificationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PasswordError.class)
    public ResponseEntity<ExceptionDTO> handlePasswordError(PasswordError ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(errorMessage) );
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ExceptionDTO> handleUserNotFound(UserNotFound ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(errorMessage) );
    }

    @ExceptionHandler(ReportNotFound.class)
    public ResponseEntity<ExceptionDTO> handleReportNotFound(ReportNotFound ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(errorMessage) );
    }

    @ExceptionHandler(BecaInvalid.class)
    public ResponseEntity<ExceptionDTO> handleBecaInvalid(BecaInvalid ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(errorMessage) );
    }


    @ExceptionHandler(NotProfessional.class)
    public ResponseEntity<ExceptionDTO> handleNotProfessional(NotProfessional ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionDTO(errorMessage) );
    }

    @ExceptionHandler(UserNameAlreadyExist.class)
    public ResponseEntity<ExceptionDTO> handleUserNameAlreadyExist(UserNameAlreadyExist ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDTO(errorMessage) );
    }

    @ExceptionHandler(NotValidTypeAppointment.class)
    public ResponseEntity<ExceptionDTO> handleNotValidTypeAppointment(NotValidTypeAppointment ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(errorMessage) );
    }

    @ExceptionHandler(HasNoAvailableDates.class)
    public ResponseEntity<ExceptionDTO> handleNotValidTypeAppointment(HasNoAvailableDates ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(errorMessage) );

    @ExceptionHandler(SettingNotFound.class)
    public ResponseEntity<ExceptionDTO> handleSettingNotFound(SettingNotFound ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(errorMessage) );

    }

}
