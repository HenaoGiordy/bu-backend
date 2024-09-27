package com.univalle.bubackend.services;

import com.univalle.bubackend.DTOs.auth.UserRequest;
import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;

@Service
public class UserServiceImpl {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(UserRequest userRequest) {
        Optional<UserEntity> existingUser = userEntityRepository.findByUsername(userRequest.username());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        Set<Role> roles = userRequest.roles();
        if (roles == null || roles.isEmpty()) {
            throw new RuntimeException("Debe proporcionar al menos un rol para el usuario.");
        }

        UserEntity user = UserEntity.builder()
                .name(userRequest.name())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .username(userRequest.username())
                .password(passwordEncoder.encode(userRequest.password()))
                .plan(userRequest.plan())
                .roles(roles)
                .build();

        return userEntityRepository.save(user);
    }

    public UserEntity findStudentsByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);

        UserEntity user = optionalUser.orElseThrow(() -> new RuntimeException("ERROR"));

        return user;
    }

}
