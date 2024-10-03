package com.univalle.bubackend.repository;

import com.univalle.bubackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT u FROM UserEntity u JOIN u.reservations r WHERE r.paid = :paid AND r.data = :date")
    List<UserEntity> findAllByReservations_PaidAndDate(@Param("paid") Boolean paid, @Param("date") LocalDateTime date);

}
