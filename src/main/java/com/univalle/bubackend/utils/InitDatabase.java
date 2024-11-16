package com.univalle.bubackend.utils;

import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.RoleRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class InitDatabase {

    @Bean
    CommandLineRunner database(PasswordEncoder passwordEncoder, UserEntityRepository userEntityRepository, RoleRepository roleRepository) {
        return args -> {

            Role estudentRole = null;
            if (roleRepository.count() == 0) {
                Role adminRole = Role.builder().name(RoleName.ADMINISTRADOR).build();
                estudentRole = Role.builder().name(RoleName.ESTUDIANTE).build();
                Role odontologoRole = Role.builder().name(RoleName.ODONTOLOGO).build();
                Role enfermeroRole = Role.builder().name(RoleName.ENFERMERO).build();
                Role externoRole = Role.builder().name(RoleName.EXTERNO).build();
                Role monitoRole = Role.builder().name(RoleName.MONITOR).build();
                Role psicologoRole = Role.builder().name(RoleName.PSICOLOGO).build();
                Role funcionarioRole = Role.builder().name(RoleName.FUNCIONARIO).build();

                // Guardar todos los roles
                roleRepository.saveAll(List.of(adminRole, estudentRole, odontologoRole, enfermeroRole, externoRole, monitoRole, psicologoRole, funcionarioRole));
            }

            // Recuperar los roles persistidos desde la base de datos
            Role adminRole = roleRepository.findByName(RoleName.ADMINISTRADOR).orElseThrow();
            Role enfermero = roleRepository.findByName(RoleName.ENFERMERO).orElseThrow();
            Role psicologo = roleRepository.findByName(RoleName.PSICOLOGO).orElseThrow();
            Role estudiante = roleRepository.findByName(RoleName.ESTUDIANTE).orElseThrow();
            Role monitor = roleRepository.findByName(RoleName.MONITOR).orElseThrow();
            Role odontologo = roleRepository.findByName(RoleName.ODONTOLOGO).orElseThrow();


            // Crear usuarios asignando los roles gestionados por JPA
            UserEntity adminUser = UserEntity.builder()
                    .name("admin")
                    .lastName("Bienestar")
                    .username("admin")
                    .email("aaaaa")
                    .plan("Bienestar Universitario")
                    .roles(Set.of(adminRole))  // Usar el role recuperado y gestionado
                    .password(passwordEncoder.encode("admin"))
                    .build();

            UserEntity profesionalUser = UserEntity.builder()
                    .name("admin")
                    .lastName("Bienestar")
                    .username("enfermero1")
                    .email("giordy@gmail.com")
                    .plan("Bienestar Universitario")
                    .roles(Set.of(enfermero))  // Usar el role recuperado y gestionado
                    .password(passwordEncoder.encode("enfermero"))
                    .build();

            UserEntity enfermero2 = UserEntity.builder()
                    .name("admin")
                    .lastName("Bienestar")
                    .username("enfermero2")
                    .email("henaogiordy@gmail.com")
                    .plan("Bienestar Universitario")
                    .roles(Set.of(enfermero))  // Usar el role recuperado y gestionado
                    .password(passwordEncoder.encode("enfermero2"))
                    .build();

            UserEntity psicologo2 = UserEntity.builder()
                    .name("psicologo_nombre")
                    .lastName("psicologo_apellido")
                    .username("psicologo")
                    .email("henao@gmail.com")
                    .plan("psicología")
                    .roles(Set.of(psicologo))  // Usar el role recuperado y gestionado
                    .password(passwordEncoder.encode("psicologo"))
                    .build();

            UserEntity odontologo2 = UserEntity.builder()
                    .name("odontologo_nombre")
                    .lastName("odontologo_apellido")
                    .username("odontologo")
                    .email("odontologo@gmail.com")
                    .plan("odontólogia")
                    .roles(Set.of(odontologo))  // Usar el role recuperado y gestionado
                    .password(passwordEncoder.encode("odontologo"))
                    .build();

            UserEntity estudiantelUser = UserEntity.builder()
                    .name("admin")
                    .lastName("Bienestar")
                    .username("estudiante")
                    .email("estudiante@gmail.com")
                    .plan("Bienestar Universitario")
                    .roles(Set.of(estudiante))  // Usar el role recuperado y gestionado
                    .password(passwordEncoder.encode("estudiante"))
                    .build();

            UserEntity monitorUser = UserEntity.builder()
                    .name("admin")
                    .lastName("Bienestar")
                    .username("monitor")
                    .email("monitor@gmail.com")
                    .plan("Bienestar Universitario")
                    .roles(Set.of(estudiante ,monitor))  // Usar el role recuperado y gestionado
                    .password(passwordEncoder.encode("monitor"))
                    .build();



            userEntityRepository.saveAll(List.of(adminUser, profesionalUser, estudiantelUser, monitorUser, enfermero2, psicologo2, odontologo2));
        };
    }
}
