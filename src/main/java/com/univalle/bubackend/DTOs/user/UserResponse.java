package com.univalle.bubackend.DTOs.user;

import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.UserEntity;

import java.util.Set;

public record UserResponse(
        Integer id,
        String username,
        String name,
        String email,
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
                userEntity.getPlan(),
                userEntity.getRoles(),
                userEntity.getIsActive()
        );
    }
}