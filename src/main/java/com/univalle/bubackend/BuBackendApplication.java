package com.univalle.bubackend;

import com.univalle.bubackend.prueba.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class BuBackendApplication {

    @Autowired
    private TestDataService testDataService;

    public static void main(String[] args) {
        SpringApplication.run(BuBackendApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createTestData() {
        testDataService.createTestData();
    }
}