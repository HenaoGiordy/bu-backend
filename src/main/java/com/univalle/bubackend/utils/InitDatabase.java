package com.univalle.bubackend.utils;

import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.RoleName;
import com.univalle.bubackend.models.UserEntity;
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
    CommandLineRunner database(PasswordEncoder passwordEncoder, UserEntityRepository userEntityRepository) {
        return args -> {
            Role adminRole = Role.builder()
                    .name(RoleName.ADMINISTRADOR)
                    .build();

            UserEntity adminUser = UserEntity.builder()
                    .name("admin")
                    .lastName("Bienestar")
                    .username("admin")
                    .email("admin@admin.com")
                    .plan("Bienestar Universitario")
                    .roles(Set.of(adminRole))
                    .password(passwordEncoder.encode("admin"))
                    .build();

            UserEntity adminUser2 = UserEntity.builder()
                    .name("admin")
                    .lastName("Bienestar")
                    .username("giordy")
                    .email("henaogiordy@gmail.com")
                    .plan("Bienestar Universitario")
                    .roles(Set.of(adminRole))
                    .password(passwordEncoder.encode("admin"))
                    .build();

            userEntityRepository.saveAll(List.of(adminUser, adminUser2));
        };
    }
}
