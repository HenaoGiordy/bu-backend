package com.univalle.bubackend.services.student;

import com.univalle.bubackend.DTOs.user.EditUserRequest;
import com.univalle.bubackend.DTOs.user.EditUserResponse;
import com.univalle.bubackend.DTOs.user.UserRequest;
import com.univalle.bubackend.DTOs.user.UserResponse;
import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.RoleRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;


import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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

    public List<UserResponse> importUsers(MultipartFile file, RoleName roleName) {
        List<UserResponse> users = new ArrayList<>();
        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withHeader());

            for (CSVRecord record : csvParser) {
                // Verificación de valores
                String username = record.get("username");
                String name = record.get("name");
                String lastName = record.get("lastName");
                String email = record.get("email");
                String plan = record.get("plan");

                // Validaciones para asegurar que los campos no estén vacíos
                if (username == null || username.trim().isEmpty()) {
                    throw new RuntimeException("El campo 'username' está vacío en el archivo CSV.");
                }
                if (name == null || name.trim().isEmpty()) {
                    throw new RuntimeException("El campo 'name' está vacío en el archivo CSV.");
                }
                if (lastName == null || lastName.trim().isEmpty()) {
                    throw new RuntimeException("El campo 'lastName' está vacío en el archivo CSV.");
                }
                if (email == null || email.trim().isEmpty()) {
                    throw new RuntimeException("El campo 'email' está vacío en el archivo CSV.");
                }
                if (plan == null || plan.trim().isEmpty()) {
                    throw new RuntimeException("El campo 'plan' está vacío en el archivo CSV.");
                }

                // Crear UserRequest
                UserRequest userRequest = new UserRequest(
                        username.trim(),
                        name.trim(),
                        lastName.trim(),
                        email.trim(),
                        null, // Password será generado después
                        plan.trim(),
                        Set.of(getRole(roleName)) // Obtener el rol de la función getRole
                );

                UserResponse userResponse = importUser(userRequest);

                users.add(userResponse);
            }

            csvParser.close();

        } catch (Exception e) {
            throw new RuntimeException("Error en el archivo CSV: " + e.getMessage());
        }

        return users;
    }

    private Role getRole(RoleName roleName) {
        return  roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("No se encontro el rol"));
    }

    public UserResponse importUser(UserRequest userRequest) {
        Optional<UserEntity> userOpt = userEntityRepository.findByUsername(userRequest.username());
        if (userOpt.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        String generatedPassword = generatePassword(userRequest.name(), userRequest.username(), userRequest.lastName());

        UserEntity newUser = UserEntity.builder()
                .username(userRequest.username())
                .name(userRequest.name())
                .lastName(userRequest.lastName())
                .plan(userRequest.plan())
                .password(passwordEncoder.encode(generatedPassword))
                .email(userRequest.email())
                .roles(userRequest.roles())
                .isActive(true)
                .build();

        userEntityRepository.save(newUser);

        return new UserResponse(
                newUser.getUsername(),
                newUser.getName(),
                newUser.getEmail(),
                newUser.getPlan(),
                newUser.getRoles(),
                newUser.getIsActive());
    }

    private String generatePassword(String name, String username,
                                    String lastName) {
        String initialName = name.substring(0, 1).toUpperCase();
        String initialLastName = lastName.substring(0, 1).toUpperCase();

        return initialName + username + initialLastName;
    }

}
