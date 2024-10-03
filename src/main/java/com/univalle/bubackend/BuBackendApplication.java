package com.univalle.bubackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuBackendApplication.class, args);
    }

}
