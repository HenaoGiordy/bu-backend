package com.univalle.bubackend.DTOs.nursing;

import com.univalle.bubackend.models.Gender;
import com.univalle.bubackend.models.UserEntity;


public record UserResponse(
        String username,
        String name,
        Integer phone,
        String plan,
        String semester,
        Gender gender
) {
    public UserResponse(UserEntity userEntity) {
        this(
                userEntity.getUsername(),
                userEntity.getName() + " " + userEntity.getEmail(),
                Math.toIntExact(userEntity.getPhone()),
                userEntity.getPlan(),
                userEntity.getSemester(),
                userEntity.getGender()
        );
    }
}