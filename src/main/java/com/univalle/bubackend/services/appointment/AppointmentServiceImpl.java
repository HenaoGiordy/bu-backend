package com.univalle.bubackend.services.appointment;

import com.univalle.bubackend.DTOs.appointment.RequestAvailableDate;
import com.univalle.bubackend.DTOs.appointment.ResponseAvailableDate;
import com.univalle.bubackend.exceptions.NotProfessional;
import com.univalle.bubackend.exceptions.change_password.UserNotFound;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.AvailableDatesRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    private AvailableDatesRepository availableDatesRepository;
    private UserEntityRepository userEntityRepository;

    @Override
    public ResponseAvailableDate availableDatesAssign(RequestAvailableDate requestAvailableDate) {
        UserEntity professional = userEntityRepository.findById(requestAvailableDate.professionalId()).orElseThrow(
                () -> new UserNotFound("No se encontró un usuario")
        );

        Set<RoleName> acceptableRoles = EnumSet.of(RoleName.PSICOLOGO, RoleName.ENFERMERO, RoleName.ODONTOLOGO);

        if(professional.getRoles().stream().noneMatch(role -> acceptableRoles.contains(role.getName()))){
            throw new NotProfessional("Debes ser un profesional para asignar un horario" + acceptableRoles);
        }



        return null;
    }
}
