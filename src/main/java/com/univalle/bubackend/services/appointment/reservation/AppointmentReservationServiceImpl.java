package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.*;
import com.univalle.bubackend.DTOs.user.UserResponse;
import com.univalle.bubackend.exceptions.appointment.DateNotAvailable;
import com.univalle.bubackend.exceptions.appointment.IsExterno;
import com.univalle.bubackend.exceptions.appointment.NoAvailableDateFound;
import com.univalle.bubackend.exceptions.change_password.UserNotFound;
import com.univalle.bubackend.models.AppointmentReservation;
import com.univalle.bubackend.models.AvailableDates;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.AppointmentReservationRepository;
import com.univalle.bubackend.repository.AvailableDatesRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentReservationServiceImpl implements IAppointmentReservationService {

    private UserEntityRepository userEntityRepository;
    private AppointmentReservationRepository appointmentReservationRepository;
    private AvailableDatesRepository availableDatesRepository;


    @Override
    public ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findById(requestAppointmentReservation.pacientId());
        UserEntity userEntity = userEntityOptional.orElseThrow(() -> new UserNotFound("No se encontró el estudiante"));

        Optional<AvailableDates> availableDatesOptional = availableDatesRepository.findById(requestAppointmentReservation.availableDateId());
        AvailableDates availableDates = availableDatesOptional.orElseThrow(()-> new NoAvailableDateFound("No se encontró la fecha disponible"));

        if(userEntity.getRoles().stream().anyMatch((x)-> x.getName() == RoleName.EXTERNO)){
            throw new IsExterno("Los externos no pueden pedir cita para poder reservar una cita");
        }

        if(!availableDates.getAvailable()){
            throw new DateNotAvailable("La fecha ya no está disponible");
        }

        AppointmentReservation appointmentReservation = AppointmentReservation.builder()
                .estudiante(userEntity)
                .availableDates(availableDates)
                .build();

        availableDates.setAvailable(false);

        availableDatesRepository.save(availableDates);
        appointmentReservationRepository.save(appointmentReservation);

        return new ResponseAppointmentReservation("Cita reservada con éxito", new AvailableDateDTO(availableDates), new UserResponse(userEntity));
    }

    @Override
    public ResponseAppointmentReservationProfessional allAppointmentProfessional(Integer professionalId) {
        Optional<List<AppointmentReservation>> appointmentReservationsOpt = appointmentReservationRepository.findByAvailableDates_ProfessionalId(professionalId);
        List<AppointmentReservationDTO> appointmentReservationDTOS = appointmentReservationsOpt.orElseThrow(()-> new UserNotFound("Usuario no encontrado"))
                .stream()
                .map(AppointmentReservationDTO::new).toList();

        return new ResponseAppointmentReservationProfessional(appointmentReservationDTOS);
    }

    @Override
    public ResponseAppointmentReservationProfessional allAppointmentEstudiante(Integer estudianteId) {

        Optional<List<AppointmentReservation>> appointmentReservations = appointmentReservationRepository.findByEstudiante_Id(estudianteId);
        List<AppointmentReservationDTO> appointmentReservationDTOS = appointmentReservations.orElseThrow(()-> new UserNotFound("Usuario no encontrado"))
                .stream()
                .map(AppointmentReservationDTO::new).toList();

        return new ResponseAppointmentReservationProfessional(appointmentReservationDTOS);
    }
}
