package com.univalle.bubackend.services.nursing;

import com.univalle.bubackend.DTOs.nursing.ActivityLogRequest;
import com.univalle.bubackend.DTOs.nursing.ActivityLogResponse;
import com.univalle.bubackend.DTOs.nursing.ActivityNursingResponse;
import com.univalle.bubackend.DTOs.nursing.UserResponse;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.models.NursingActivityLog;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.NursingActivityRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class NursingActivityLogImpl implements INursingActivityLog {

    private UserEntityRepository userEntityRepository;
    private NursingActivityRepository nursingActivityLogRepository;

    @Override
    public UserResponse findStudentsByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);
        UserEntity user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return new UserResponse(user);
    }

    @Override
    public ActivityLogResponse registerActivity(ActivityLogRequest request) {

        UserEntity user = userEntityRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setPhone(Long.valueOf(request.phone()));
        user.setSemester(request.semester());
        user.setGender(request.gender());
        userEntityRepository.save(user);

        NursingActivityLog nursingActivityLog = NursingActivityLog.builder()
                .user(user)
                .date(request.date())
                .diagnostic(request.diagnostic())
                .conduct(request.conduct())
                .build();

        nursingActivityLogRepository.save(nursingActivityLog);

        return new ActivityLogResponse(
                nursingActivityLog.getId(),
                nursingActivityLog.getDate().toLocalDate(),
                nursingActivityLog.getDate().toLocalTime(),
                user.getUsername(),
                user.getName() + " " + user.getLastName(),
                user.getPhone(),
                user.getPlan(),
                user.getSemester(),
                user.getGender(),
                nursingActivityLog.getDiagnostic(),
                nursingActivityLog.getConduct()
        );
    }

    @Override
    public List<ActivityNursingResponse> activitiesNursing(String username) {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<NursingActivityLog> activities = nursingActivityLogRepository.findAllByUserUsernameOrderByIdDesc(username);

        return activities.stream()
                .map(activity -> new ActivityNursingResponse(
                        activity.getId(),
                        activity.getDate().toLocalDate(),
                        activity.getDate().toLocalTime(),
                        new UserResponse(user),
                        activity.getDiagnostic(),
                        activity.getConduct()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ActivityNursingResponse getActivityNursing(Integer id) {
        NursingActivityLog activity = nursingActivityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad de enfermeria no encontrada"));
        return new ActivityNursingResponse(activity);
    }
}
