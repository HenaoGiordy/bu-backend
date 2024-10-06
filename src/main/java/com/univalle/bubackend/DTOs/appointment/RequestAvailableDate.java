package com.univalle.bubackend.DTOs.appointment;

import java.util.List;

public record RequestAvailableDate(List<AvailableDateDTO> availableDates, Integer professionalId) {
}
