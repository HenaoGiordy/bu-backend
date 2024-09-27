package com.univalle.bubackend.DTOs.auth;

import com.univalle.bubackend.models.Role;

import java.util.Set;

public record UserResponse(
        String code,
        String name,
        String email,
        String plan,
        Set<Role>roles,
        boolean isActive
) {}