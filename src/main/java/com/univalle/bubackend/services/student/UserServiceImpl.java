package com.univalle.bubackend.services.student;

import com.univalle.bubackend.DTOs.user.EditUserRequest;
import com.univalle.bubackend.DTOs.user.EditUserResponse;
import com.univalle.bubackend.DTOs.user.UserRequest;
import com.univalle.bubackend.DTOs.user.UserResponse;
import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest userRequest) {
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

        userEntityRepository.save(user);
        return new UserResponse(user.getUsername(), user.getName(), user.getEmail(), user.getPlan(), user.getRoles(), user.getIsActive());
    }

    public UserResponse findStudentsByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);

        UserEntity user = optionalUser.orElseThrow(() -> new RuntimeException("ERROR"));

        return new UserResponse(user.getUsername(), user.getName(), user.getEmail(), user.getPlan(), user.getRoles(), user.getIsActive());
    }

    public EditUserResponse editUser(EditUserRequest editUserRequest) {
        Optional<UserEntity> optionalUser = userEntityRepository.findById(editUserRequest.id());
        UserEntity user = optionalUser.orElseThrow(() -> new RuntimeException("ERROR"));
        
        user.setName(editUserRequest.name());
        user.setLastName(editUserRequest.lastName());
        user.setEmail(editUserRequest.email());
        user.setPlan(editUserRequest.plan());
        user.setRoles(editUserRequest.roles());
        user.setIsActive(editUserRequest.isActive());
        user.setLunchBeneficiary(editUserRequest.lunchBeneficiary());
        user.setRoles(editUserRequest.roles());
        

        userEntityRepository.save(user);

        return new EditUserResponse("Usuario editado satisfactoriamente", new UserResponse(user.getUsername(), user.getName(), user.getEmail(), user.getPlan(), user.getRoles(), user.getIsActive()));
    }

}
