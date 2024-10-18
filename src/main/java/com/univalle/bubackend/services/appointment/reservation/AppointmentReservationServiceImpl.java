package com.univalle.bubackend.services.appointment.reservation;

import com.univalle.bubackend.DTOs.appointment.*;
import com.univalle.bubackend.DTOs.user.UserResponse;
import com.univalle.bubackend.exceptions.appointment.CantReserveMoreAppointments;
import com.univalle.bubackend.exceptions.appointment.HaveAnAppoinmentPending;
import com.univalle.bubackend.exceptions.appointment.DateNotAvailable;
import com.univalle.bubackend.exceptions.appointment.IsExterno;
import com.univalle.bubackend.exceptions.appointment.NoAvailableDateFound;
import com.univalle.bubackend.exceptions.change_password.UserNotFound;
import com.univalle.bubackend.exceptions.appointment.ReservationNotFoud;
import com.univalle.bubackend.models.*;
import com.univalle.bubackend.repository.AppointmentReservationRepository;
import com.univalle.bubackend.repository.AvailableDatesRepository;
import com.univalle.bubackend.repository.SettingRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentReservationServiceImpl implements IAppointmentReservationService {

    private UserEntityRepository userEntityRepository;
    private AppointmentReservationRepository appointmentReservationRepository;
    private AvailableDatesRepository availableDatesRepository;
    private TaskScheduler taskScheduler;
    private SettingRepository settingRepository;


    @Override
    public ResponseAppointmentReservation reserveAppointment(RequestAppointmentReservation requestAppointmentReservation) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findById(requestAppointmentReservation.pacientId());
        UserEntity userEntity = userEntityOptional.orElseThrow(() -> new UserNotFound("No se encontró el estudiante"));
        Optional<AvailableDates> availableDatesOptional = availableDatesRepository.findById(requestAppointmentReservation.availableDateId());
        AvailableDates availableDates = availableDatesOptional.orElseThrow(()-> new NoAvailableDateFound("No se encontró la fecha disponible"));

        Integer count = 0;
        if(availableDates.getTypeAppointment() == TypeAppointment.PSICOLOGIA){
            if(LocalDateTime.now().getMonth().getValue() > Month.JUNE.getValue()) {
                LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear(), 6, 1, 0, 0); // 1 de junio
                LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59);//31 de Diciembre
                count = appointmentReservationRepository.countAppointmentReservationByEstudiante_IdAndAvailableDates_DateTime(userEntity, startDate, endDate, availableDates.getTypeAppointment());

            }else{
                LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0); // 1 de enero
                LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), 5, 31, 23, 59); // 30 de junio
                count = appointmentReservationRepository.countAppointmentReservationByEstudiante_IdAndAvailableDates_DateTime(userEntity, startDate, endDate, availableDates.getTypeAppointment());
            }
            if(count == 4){
                throw new CantReserveMoreAppointments("Ya tuviste 4 citas de " + availableDates.getTypeAppointment().toString()+ " este semestre");
            }
        }



        Optional<AppointmentReservation> appointmentReservationOpt = appointmentReservationRepository.findByEstudiante_IdAndPendingAppointmentTrue(userEntity.getId());

        if(appointmentReservationOpt.isPresent()) {
            AppointmentReservation appointmentReservation = appointmentReservationOpt.get();
            throw new HaveAnAppoinmentPending("Tienes una cita pendiente de " + appointmentReservation.getAvailableDates().getTypeAppointment().toString());
        }

//        if ((!LocalDateTime.now().isBefore(availableDates.getDateTime()))) {
//            throw new DateNotAvailable("Ya pasó la fecha de reserva");
//        }
//
//        // Validar si falta menos de 1 hora para la cita
//        if (availableDates.getDateTime().isBefore(LocalDateTime.now().plusMinutes(60))) {
//            throw new DateNotAvailable("No puedes reservar la fecha con menos de una hora de antelación");
//        }

        if(userEntity.getRoles().stream().anyMatch((x)-> x.getName() == RoleName.EXTERNO)){
            throw new IsExterno("Los externos no pueden una cita");
        }

        if(!availableDates.getAvailable()){
            throw new DateNotAvailable("La fecha ya no está disponible");
        }

        AppointmentReservation appointmentReservation = AppointmentReservation.builder()
                .estudiante(userEntity)
                .availableDates(availableDates)
                .build();

        availableDates.setAvailable(false);

        if(requestAppointmentReservation.phone() != null) {
            userEntity.setPhone(requestAppointmentReservation.phone());
        }
        if(requestAppointmentReservation.eps() != null){
            userEntity.setEps(requestAppointmentReservation.eps());
        }
        if(requestAppointmentReservation.semester() != null){
            userEntity.setSemester(requestAppointmentReservation.semester());
        }
        userEntityRepository.save(userEntity);
        availableDatesRepository.save(availableDates);
        AppointmentReservation appointmentReservation1= appointmentReservationRepository.save(appointmentReservation);

        taskScheduler.schedule(()->{
            appointmentReservation1.setPendingAppointment(false);
            appointmentReservationRepository.save(appointmentReservation1);
            }, convertToInstant(appointmentReservation1.getAvailableDates().getDateTime()));

        return new ResponseAppointmentReservation("Cita reservada con éxito", new AvailableDateDTO(availableDates), new UserResponse(userEntity));
    }

    private Instant convertToInstant(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return localDateTime.atZone(zoneId).toInstant();
    }

    @Override
    public ResponseAppointmentReservationProfessional allAppointmentProfessional(Integer professionalId) {
        Optional<List<AppointmentReservation>> appointmentReservationsOpt = appointmentReservationRepository.findByAvailableDates_ProfessionalId(professionalId);
        List<AppointmentReservationProfessionalDTO> appointmentReservationDTOS = appointmentReservationsOpt.orElseThrow(()-> new UserNotFound("Usuario no encontrado"))
                .stream()
                .map(AppointmentReservationProfessionalDTO::new).toList();

        return new ResponseAppointmentReservationProfessional(appointmentReservationDTOS);
    }

    @Override
    public ResponseAppointmentReservationStudent allAppointmentEstudiante(Integer estudianteId) {

        Optional<List<AppointmentReservation>> appointmentReservations = appointmentReservationRepository.findByEstudiante_Id(estudianteId);
        List<AppointmentReservationStudentDTO> appointmentReservationDTOS = appointmentReservations.orElseThrow(()-> new UserNotFound("Usuario no encontrado"))
                .stream()
                .map(AppointmentReservationStudentDTO::new).toList();

        return new ResponseAppointmentReservationStudent(appointmentReservationDTOS);
    }

    @Transactional
    @Override
    public ResponseAppointmentCancel cancelReservation(Integer id) {
        Optional<AppointmentReservation> appointmentReservationOpt = appointmentReservationRepository.findById(id);


        AppointmentReservation appointmentReservation = appointmentReservationOpt.orElseThrow(() ->
                new ReservationNotFoud("No se encontró la reserva a cancelar o ya se canceló"));

        AvailableDates availableDates = appointmentReservation.getAvailableDates();

        availableDates.setAvailable(true);

        availableDatesRepository.save(availableDates);
        appointmentReservationRepository.delete(appointmentReservation);
        return new ResponseAppointmentCancel("Se cancelado la reserva", appointmentReservation.getAvailableDates());
    }
}
