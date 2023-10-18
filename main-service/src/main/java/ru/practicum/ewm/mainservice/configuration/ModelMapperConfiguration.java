package ru.practicum.ewm.mainservice.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {
  @Bean
  @Qualifier("modelMapperCompilationService")
  public ModelMapper modelMapperCompilationService() {
    final ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
               .setPropertyCondition(Conditions.isNotNull());
    return modelMapper;
  }

  @Bean
  @Qualifier("modelMapperEventService")
  public ModelMapper modelMapperEventService() {
    final ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
               .setPropertyCondition(Conditions.isNotNull())
               .setPropertyCondition(context -> {
                                       Object sourceValue = context.getSource();
                                       Object destinationValue = context.getDestination();
                                       return sourceValue != null && !sourceValue.equals(destinationValue);
                                     }
               );
    return modelMapper;
  }
}
