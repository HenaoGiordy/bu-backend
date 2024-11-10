package com.univalle.bubackend.services.odontology;

import com.univalle.bubackend.DTOs.odontology.*;
import com.univalle.bubackend.DTOs.user.UserRequest;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.exceptions.nursing.FieldException;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.models.VisitOdontologyLog;
import com.univalle.bubackend.repository.OdontologyVisitRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import com.univalle.bubackend.services.user.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class OdontologyVisitLogImpl implements IOdontologyVisitLog {

    private final UserEntityRepository userEntityRepository;
    private final OdontologyVisitRepository odontologyVisitRepository;
    private final UserServiceImpl userService;

    @Override
    public UserResponse findStudentsByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);
        UserEntity user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return new UserResponse(user);
    }

    @Override
    public VisitLogResponse registerVisit(VisitLogRequest request) {
        Optional<UserEntity> userCondition = userEntityRepository.findByUsername(request.username());

        if (userCondition.isEmpty()) {
            Set<String> roles = Set.of("EXTERNO");
            UserRequest userRequest = new UserRequest(
                    request.username(),
                    request.name(),
                    request.lastname(),
                    null,
                    null,
                    request.plan(),
                    roles,
                    null
            );
            userService.createUser(userRequest);

        }


        UserEntity user = userEntityRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        VisitOdontologyLog visitOdontologyLog = VisitOdontologyLog.builder()
                .user(user)
                .date(request.date().atStartOfDay())
                .reason(request.reason())
                .description(request.description())
                .build();

        odontologyVisitRepository.save(visitOdontologyLog);

        return new VisitLogResponse(
                visitOdontologyLog.getId(),
                visitOdontologyLog.getDate().toLocalDate(),
                visitOdontologyLog.getDate().toLocalTime(),
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getPlan(),
                visitOdontologyLog.getReason(),
                visitOdontologyLog.getDescription()
        );
    }

    @Override
    public VisitOdontologyResponse visitsOdonotology(String username, LocalDate startDate, LocalDate endDate, Pageable pageable) {

        if (username == null && (startDate == null || endDate == null)) {
            throw new FieldException("Debe suministrar el nombre de usuario o el rango de fechas para realizar la búsqueda");
        }

        UserResponse user = new UserResponse(null, null, null, null);
        Page<VisitOdontologyLog> visitResponses;

        // Si ambos están presentes, filtrar por username y fecha
        if (username != null && startDate != null && endDate != null) {
            UserEntity usr = userEntityRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            user = new UserResponse(usr);

            visitResponses = odontologyVisitRepository.findAllByUserUsernameAndDateBetween(
                    username, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), pageable);

            // Solo por username
        } else if (username != null) {
            UserEntity usr = userEntityRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            user = new UserResponse(usr);
            visitResponses = odontologyVisitRepository.findAllByUserUsername(username, pageable);

            // Solo por rango de fechas
        } else {
            visitResponses = odontologyVisitRepository.findAllByDateBetween(
                    startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), pageable);
        }

        UserResponse finalUser = user;
        visitResponses
                .map(visit -> new VisitResponse(
                        visit.getDate(),
                        finalUser.name(),
                        finalUser.username(),
                        finalUser.plan(),
                        visit.getReason(),
                        visit.getDescription()
                ));

        return new VisitOdontologyResponse(visitResponses, user);
    }

    @Override
    public VisitResponse getOdontologyVisit(Long id) {
        VisitOdontologyLog visit = odontologyVisitRepository.findById(id);
        UserResponse user = new UserResponse(visit.getUser());
        return new VisitResponse(
                visit.getDate(),
                user.name(),
                user.username(),
                user.plan(),
                visit.getReason(),
                visit.getDescription()
        );
    }

}
