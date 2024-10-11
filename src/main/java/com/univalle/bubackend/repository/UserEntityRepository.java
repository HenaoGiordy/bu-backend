package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN u.reservations r WHERE r.lunch = true AND r.paid = true AND r.data BETWEEN :startOfDay AND :endOfDay")
    List<UserEntity> findUserLunchPaid(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT u FROM UserEntity u JOIN u.reservations r WHERE r.snack = true AND r.paid = true AND r.data BETWEEN :startOfDay AND :endOfDay")
    List<UserEntity> findUserSnackPaid(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    List<UserEntity> findByLunchBeneficiaryTrueOrSnackBeneficiaryTrue();
}
