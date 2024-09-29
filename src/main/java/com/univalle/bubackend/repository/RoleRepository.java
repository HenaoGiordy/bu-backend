package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.Role;
import com.univalle.bubackend.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
