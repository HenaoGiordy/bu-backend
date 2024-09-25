package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
