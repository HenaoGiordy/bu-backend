package com.univalle.bubackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduleConfig {

    @Primary
    @Bean
    TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
