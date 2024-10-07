package com.univalle.bubackend.services.appointment;

import com.univalle.bubackend.DTOs.appointment.RequestAllAvailable;
import com.univalle.bubackend.DTOs.appointment.RequestAvailableDate;
import com.univalle.bubackend.DTOs.appointment.ResponseAllAvailableDates;
import com.univalle.bubackend.DTOs.appointment.ResponseAvailableDate;

public interface IAppointmentService {
    ResponseAvailableDate availableDatesAssign(RequestAvailableDate requestAvailableDate);
    ResponseAllAvailableDates getAllDatesProfessional(Integer professionalId);
}
