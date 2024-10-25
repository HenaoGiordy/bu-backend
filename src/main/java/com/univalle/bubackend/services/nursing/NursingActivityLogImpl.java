package com.univalle.bubackend.services.nursing;

import com.univalle.bubackend.DTOs.nursing.ActivityLogRequest;
import com.univalle.bubackend.DTOs.nursing.ActivityLogResponse;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.models.NursingActivityLog;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.NursingActivityRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
@Transactional
public class NursingActivityLogImpl implements INursingActivityLog {

    private UserEntityRepository userEntityRepository;
    private NursingActivityRepository nursingActivityLogRepository;

    @Override
    public ActivityLogResponse registerActivity(ActivityLogRequest request) {

        UserEntity user = userEntityRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setPhone(request.phone());
        user.setSemester(request.semester());
        user.setGender(request.gender());
        userEntityRepository.save(user);

        NursingActivityLog nursingActivityLog = NursingActivityLog.builder()
                .user(user)
                .date(LocalDateTime.now())
                .time(LocalTime.now())
                .diagnostic(request.diagnostic())
                .conduct(request.conduct())
                .build();

        nursingActivityLogRepository.save(nursingActivityLog);

        return new ActivityLogResponse(
                nursingActivityLog.getId(),
                nursingActivityLog.getDate().toLocalDate(),
                user.getUsername(),
                user.getName(),
                user.getPhone(),
                user.getPlan(),
                user.getSemester(),
                user.getGender(),
                nursingActivityLog.getDiagnostic(),
                nursingActivityLog.getConduct()
        );
    }
}
