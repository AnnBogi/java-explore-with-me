package ru.practicum.ewm.mainservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.practicum.mainservice.exception.configuration.ExceptionHandlerConfiguration;

@SpringBootApplication(scanBasePackages = {"ru.practicum.ewm.mainservice", "ru.practicum.ewm.stats.client"})
@Import(ExceptionHandlerConfiguration.class)
public class MainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

}