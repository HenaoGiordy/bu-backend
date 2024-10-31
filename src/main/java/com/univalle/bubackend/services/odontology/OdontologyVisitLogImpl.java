package com.univalle.bubackend.services.odontology;

import com.univalle.bubackend.DTOs.odontology.UserResponse;
import com.univalle.bubackend.DTOs.odontology.VisitLogRequest;
import com.univalle.bubackend.DTOs.odontology.VisitLogResponse;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.models.VisitOdontologyLog;
import com.univalle.bubackend.repository.OdontologyVisitRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class OdontologyVisitLogImpl implements IOdontologyVisitLog {

    private UserEntityRepository userEntityRepository;
    private OdontologyVisitRepository odontologyVisitRepository;

    @Override
    public UserResponse findStudentsByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);
        UserEntity user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return new UserResponse(user);
    }

    @Override
    public VisitLogResponse registerVisit(VisitLogRequest request) {

        UserEntity user = userEntityRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        VisitOdontologyLog visitOdontologyLog = VisitOdontologyLog.builder()
                .user(user)
                .date(request.date())
                .reason(request.reason())
                .description(request.description())
                .build();

        odontologyVisitRepository.save(visitOdontologyLog);

        return new VisitLogResponse(
                visitOdontologyLog.getId(),
                visitOdontologyLog.getDate().toLocalDate(),
                visitOdontologyLog.getDate().toLocalTime(),
                user.getUsername(),
                user.getName() + " " + user.getLastName(),
                user.getPlan(),
                visitOdontologyLog.getReason(),
                visitOdontologyLog.getDescription()
        );
    }
}
