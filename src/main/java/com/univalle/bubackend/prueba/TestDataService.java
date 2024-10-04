package com.univalle.bubackend.prueba;

import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TestDataService {

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public void createTestData() {
        UserEntity user1 = new UserEntity();
        user1.setUsername("usuario1");
        user1.setPassword("password1");
        user1.setName("Nombre1");
        user1.setLastName("Apellido1");
        user1.setEmail("usuario1@example.com");
        user1.setPlan("Plan A");

        Reservation reservation1 = new Reservation();
        reservation1.setData(LocalDateTime.now());
        reservation1.setTime(LocalTime.now());
        reservation1.setPaid(true);
        reservation1.setLunch(true);
        reservation1.setSnack(false);
        reservation1.setUserEntity(user1);

        user1.setReservations(List.of(reservation1));

        UserEntity user2 = new UserEntity();
        user2.setUsername("usuario2");
        user2.setPassword("password2");
        user2.setName("Nombre2");
        user2.setLastName("Apellido2");
        user2.setEmail("usuario1@example.com");
        user2.setPlan("Plan A");

        Reservation reservation2 = new Reservation();
        reservation2.setData(LocalDateTime.now());
        reservation2.setTime(LocalTime.now());
        reservation2.setPaid(true);
        reservation2.setLunch(true);
        reservation2.setSnack(true);
        reservation2.setUserEntity(user2);

        user2.setReservations(List.of(reservation2));

        userRepository.saveAll(List.of(user1, user2));
    }
}
