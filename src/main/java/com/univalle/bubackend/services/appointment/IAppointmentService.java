package com.univalle.bubackend.services.appointment;

import com.univalle.bubackend.DTOs.appointment.*;

public interface IAppointmentService {
    ResponseAvailableDate availableDatesAssign(RequestAvailableDate requestAvailableDate);
    ResponseAllAvailableDates getAllDatesProfessional(Integer professionalId);
    ResponseDeleteAvailableDate deleteAvailableDate(Integer id);
}
