package com.univalle.bubackend.services.appointment.validations;

import com.univalle.bubackend.exceptions.appointment.NotValidTypeAppointment;
import com.univalle.bubackend.models.TypeAppointment;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class IsValidTypeAppointment {

    public void validateTypeAppointment(String typeAppointment) {
        try{
            TypeAppointment.valueOf(typeAppointment.toUpperCase());
        }catch (IllegalArgumentException ex){
            throw new NotValidTypeAppointment("Debes ingresar un tipo de cita válido");
        }

    }
}
