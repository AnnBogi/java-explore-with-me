package ru.practicum.mainservice.exception.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.mainservice.exception.ErrorHandler;

@Configuration
public class ExceptionHandlerConfiguration {
  @Bean
  public ErrorHandler errorHandler() {
    return new ErrorHandler();
  }
}
