package com.univalle.bubackend.DTOs.odontology;

import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.UserEntity;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record UserResponse(
        Integer id,
        String username,
        String name,
        String email,
        String eps,
        String semester,
        @Positive Integer phone,
        String plan,
        Set<Role>roles,
        boolean lunchBeneficiary,
        boolean snackBeneficiary,
        boolean isActive
) {
    public UserResponse(UserEntity userEntity) {
        this(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getEps(),
                userEntity.getSemester(),
                Math.toIntExact(userEntity.getPhone()),
                userEntity.getPlan(),
                userEntity.getRoles(),
                userEntity.getLunchBeneficiary(),
                userEntity.getSnackBeneficiary(),
                userEntity.getIsActive()
        );
    }
}