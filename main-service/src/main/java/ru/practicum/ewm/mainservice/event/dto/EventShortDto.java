package ru.practicum.ewm.mainservice.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventShortDto extends ResponseEventDto {
  private long commentsCount;
}