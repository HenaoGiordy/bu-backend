package com.univalle.bubackend.services.user;

import com.univalle.bubackend.DTOs.user.*;

import com.univalle.bubackend.exceptions.CSVFieldException;


import com.univalle.bubackend.exceptions.InvalidFilter;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.exceptions.change_password.PasswordError;
import com.univalle.bubackend.exceptions.users.RoleNotFound;
import com.univalle.bubackend.exceptions.change_password.UserNotFound;
import com.univalle.bubackend.exceptions.resetpassword.PasswordDoesNotMatch;
import com.univalle.bubackend.exceptions.users.UserNameAlreadyExist;
import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.RoleRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;


import java.util.*;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl {

    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        Optional<UserEntity> existingUser = userEntityRepository.findByUsername(userRequest.username());
        if (existingUser.isPresent()) {

            throw new UserNameAlreadyExist("El usuario ya está en registrado.");

        }

        try{
            userRequest.roles().forEach(RoleName::valueOf);
        }catch (IllegalArgumentException e){
            throw new RoleNotFound("El nombre de role no existe");
        }

        Set<Role> roles = userRequest.roles().stream()
                .map(roleRequest -> roleRepository.findByName(RoleName.valueOf(roleRequest))
                        .orElseThrow(() -> new RoleNotFound("No se ha creado el role " + roleRequest)))
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            throw new RoleNotFound("Debe proporcionar al menos un rol para el usuario.");
        }

        String generatedPassword = generatePassword(userRequest.name(), userRequest.username(), userRequest.lastName());

        UserEntity user = UserEntity.builder()
                .name(userRequest.name())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .username(userRequest.username())
                .password(passwordEncoder.encode(generatedPassword))
                .plan(userRequest.plan())
                .roles(roles)
                .lunchBeneficiary("Beneficiario almuerzo".equalsIgnoreCase(userRequest.beca()))
                .snackBeneficiary("Beneficiario refrigerio".equalsIgnoreCase(userRequest.beca()))
                .build();

        userEntityRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse findStudentsByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);

        UserEntity user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return new UserResponse(user);
    }

    public EditUserResponse editUser(EditUserRequest editUserRequest) {
        Optional<UserEntity> optionalUser = userEntityRepository.findById(editUserRequest.id());
        UserEntity user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setName(editUserRequest.name());
        user.setLastName(editUserRequest.lastName());
        user.setEmail(editUserRequest.email());
        user.setPlan(editUserRequest.plan());
        user.setRoles(editUserRequest.roles());
        user.setIsActive(editUserRequest.isActive());
        user.setLunchBeneficiary(editUserRequest.lunchBeneficiary());
        user.setSnackBeneficiary(editUserRequest.snackBeneficiary());
        user.setRoles(editUserRequest.roles());


        userEntityRepository.save(user);

        return new EditUserResponse("Usuario editado satisfactoriamente", new UserResponse(user));
    }


    public List<UserResponse> importUsers(MultipartFile file, RoleName roleName) {
        List<UserResponse> users = new ArrayList<>();
        try {
            String firstLine = new BufferedReader(new InputStreamReader(file.getInputStream())).readLine();
            char delimiter = detectDelimiter(firstLine);

            Reader reader = new InputStreamReader(file.getInputStream());
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(delimiter).withHeader());

            for (CSVRecord record : csvParser) {

                String username = null;
                if (record.isMapped("codigo/cedula")) {
                    username = record.get("codigo/cedula");
                } else if (record.isMapped("codigo")) {
                    username = record.get("codigo");
                } else if (record.isMapped("cedula")) {
                    username = record.get("cedula");
                }
                String name = record.get("nombre");
                String lastName = record.get("apellido");
                String email = record.get("correo");

                String plan = null;
                if (record.isMapped("plan/area")) {
                    plan = record.get("plan/area");
                } else if (record.isMapped("plan")) {
                    plan = record.get("plan");
                } else if (record.isMapped("area")) {
                    plan = record.get("area");
                }

                String nota = null;
                if (record.isMapped("Las opciones de beca son: almuerzo/refrigerio")) {
                    nota = record.get("Las opciones de beca son: almuerzo/refrigerio").trim();
                    if (nota.isEmpty()) {
                        nota = null;
                    }
                }


                String beca = record.isMapped("beca") ? record.get("beca") : null;

                if (username == null || username.trim().isEmpty()) {
                    throw new CSVFieldException("El campo 'codigo/cedula' está vacío en el archivo CSV.");
                }
                if (name == null || name.trim().isEmpty()) {
                    throw new CSVFieldException("El campo 'nombre' está vacío en el archivo CSV.");
                }
                if (lastName == null || lastName.trim().isEmpty()) {
                    throw new CSVFieldException("El campo 'apellido' está vacío en el archivo CSV.");
                }
                if (email == null || email.trim().isEmpty()) {
                    throw new CSVFieldException("El campo 'correo' está vacío en el archivo CSV.");
                }
                if (plan == null || plan.trim().isEmpty()) {
                    throw new CSVFieldException("El campo 'plan/area' está vacío en el archivo CSV.");
                }

                UserRequest userRequest = new UserRequest(
                        username.trim(),
                        name.trim(),
                        lastName.trim(),
                        email.trim(),
                        null,
                        plan.trim(),
                        Set.of(roleName.name()),
                        beca
                );

                UserResponse userResponse = importUser(userRequest);

                users.add(userResponse);
            }

            csvParser.close();

        } catch (Exception e) {
            throw new CSVFieldException("Error en el archivo CSV: " + e.getMessage());
        }

        return users;
    }

    private char detectDelimiter(String firstLine) {
        String[] delimiters = {",", ";", "\t", "|"};

        for (String delimiter : delimiters) {
            if (firstLine.split(delimiter).length > 1) {
                return delimiter.charAt(0);
            }
        }

        throw new IllegalArgumentException("No se pudo detectar un delimitador válido en el archivo CSV.");
    }

    public UserResponse importUser(UserRequest userRequest) {
        Optional<UserEntity> userOpt = userEntityRepository.findByUsername(userRequest.username());
        UserEntity newUser;



        if (userOpt.isPresent()) {
            newUser = userOpt.get();

            newUser.setName(userRequest.name());
            newUser.setLastName(userRequest.lastName());
            newUser.setEmail(userRequest.email());
            newUser.setPlan(userRequest.plan());

            String beca = userRequest.beca();
            if ("almuerzo".equalsIgnoreCase(beca)) {
                newUser.setLunchBeneficiary(true);
                newUser.setSnackBeneficiary(false);
            } else if ("refrigerio".equalsIgnoreCase(beca)) {
                newUser.setLunchBeneficiary(false);
                newUser.setSnackBeneficiary(true);
            } else {
                newUser.setLunchBeneficiary(false);
                newUser.setSnackBeneficiary(false);
            }

          //  newUser.setRoles(userRequest.roles());
            String updatePassword = generatePassword(userRequest.name(), userRequest.username(), userRequest.lastName());
            newUser.setPassword(passwordEncoder.encode(updatePassword));
            newUser.setIsActive(true);

        } else {
            String generatedPassword = generatePassword(userRequest.name(), userRequest.username(), userRequest.lastName());
            Set<Role> roles = userRequest.roles().stream().map(role -> roleRepository.findByName(RoleName.valueOf(role.toUpperCase()))
                    .orElseThrow(() -> new RoleNotFound("No se encontro el rol"))).collect(Collectors.toSet());


            newUser = UserEntity.builder()
                    .username(userRequest.username())
                    .name(userRequest.name())
                    .lastName(userRequest.lastName())
                    .plan(userRequest.plan())
                    .password(passwordEncoder.encode(generatedPassword))
                    .email(userRequest.email())
                    .roles(roles)
                    .isActive(true)
                    .lunchBeneficiary("almuerzo".equalsIgnoreCase(userRequest.beca()))
                    .snackBeneficiary("refrigerio".equalsIgnoreCase(userRequest.beca()))
                    .build();
        }

        userEntityRepository.save(newUser);

        return new UserResponse(newUser);
    }

    private String generatePassword(String name, String username,
                                    String lastName) {
        String initialName = name.substring(0, 1).toUpperCase();
        String initialLastName = lastName.substring(0, 1).toUpperCase();

        return initialName + username + initialLastName;
    }

    public PasswordResponse changePassword(PasswordRequest passwordRequest) {

        Optional<UserEntity> userOpt = userEntityRepository.findByUsername(passwordRequest.username());
        UserEntity user = userOpt.orElseThrow(() -> new UserNotFound("No se encontró el usuario"));

        if (!passwordEncoder.matches(passwordRequest.password(), user.getPassword())) {
            throw new PasswordError("La contraseña actual es incorrecta");
        }

        if (!passwordRequest.newPassword().equals(passwordRequest.confirmPassword())) {
            throw new PasswordDoesNotMatch("Las contraseñas no coinciden");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.newPassword()));
        userEntityRepository.save(user);

        return new PasswordResponse("Contraseña cambiada con exito");

    }

    public Page<ListUser> listUsers(String filter, Pageable pageable){
        Page<UserEntity> users;

        switch (filter.toLowerCase()) {
            case "beneficiarios":
                users = userEntityRepository.findBeneficiaries(pageable);
                break;
            case "funcionarios":
                users = userEntityRepository.findAllNonStudents(pageable);
                break;
            case "estudiantes":
                users = userEntityRepository.findAllStudents(pageable);
                break;
            default:
                throw new InvalidFilter("Filtro no válido");
        }

        return users.map(user -> ListUser.builder()
                .id(user.getId())
                .snackBeneficiary(user.getSnackBeneficiary())
                .lunchBeneficiary(user.getLunchBeneficiary())
                .roles(user.getRoles())
                .plan(user.getPlan())
                .email(user.getEmail())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .name(user.getName())
                .build()
        );
    }

    public void deleteBeneficiaries() {
        List<UserEntity> beneficiaries = userEntityRepository.findByLunchBeneficiaryTrueOrSnackBeneficiaryTrue();

        beneficiaries.forEach(user -> {
            user.setLunchBeneficiary(false);
            user.setSnackBeneficiary(false);
        });

        userEntityRepository.saveAll(beneficiaries);

    }

    public void deleteBeneficiary(String username){
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("No se encontro el usuario"));

        user.setLunchBeneficiary(false);
        user.setSnackBeneficiary(false);

        userEntityRepository.save(user);
    }

}
