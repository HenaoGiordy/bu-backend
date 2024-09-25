package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findMenuById(Integer id);
}
