package com.univalle.bubackend.services.appointment;

import com.univalle.bubackend.DTOs.appointment.AvailableDateDTO;
import com.univalle.bubackend.DTOs.appointment.RequestAvailableDate;
import com.univalle.bubackend.DTOs.appointment.ResponseAvailableDate;
import com.univalle.bubackend.exceptions.change_password.UserNotFound;
import com.univalle.bubackend.models.AvailableDates;
import com.univalle.bubackend.models.TypeAppointment;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.AvailableDatesRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import com.univalle.bubackend.services.appointment.validations.AppointmentDateCreationValidation;
import com.univalle.bubackend.services.appointment.validations.IsValidTypeAppointment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    private AvailableDatesRepository availableDatesRepository;
    private UserEntityRepository userEntityRepository;
    private AppointmentDateCreationValidation appointmentDateCreationValidations;
    private IsValidTypeAppointment isValidTypeAppointment;

    @Override
    public ResponseAvailableDate availableDatesAssign(RequestAvailableDate requestAvailableDate) {
        UserEntity professional = userEntityRepository.findById(requestAvailableDate.professionalId()).orElseThrow(
                () -> new UserNotFound("No se encontró un usuario")
        );


        appointmentDateCreationValidations.validateIsProfessional(professional);

        requestAvailableDate.availableDates().forEach(
                x -> isValidTypeAppointment.validateTypeAppointment(x.typeAppointment()));

        List<AvailableDates> dates = requestAvailableDate.availableDates().stream().map(x ->
                                AvailableDates.builder()
                                        .dateTime(x.dateTime())
                                        .typeAppointment(TypeAppointment.valueOf(x.typeAppointment()))
                                        .professional(professional)
                                        .build())
                                        .toList();

        availableDatesRepository.saveAll(dates);

        List<AvailableDateDTO> dateDTOS = dates.stream().map(AvailableDateDTO::new).toList();

        return new ResponseAvailableDate("Se crearon las citas", professional.getId(), dateDTOS);
    }
}
