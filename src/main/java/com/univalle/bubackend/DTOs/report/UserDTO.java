package com.univalle.bubackend.DTOs.report;

import com.univalle.bubackend.models.Role;

import java.util.Set;

public record UserDTO(String username,
                      String name,
                      String lastName,
                      String email,
                      String plan,
                      Set<Role> roles) {
}
