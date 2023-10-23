package ru.practicum.ewm.mainservice.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.UserShortDto;
import ru.practicum.mainservice.constants.Variables;

import java.time.LocalDateTime;

@ToString(callSuper = true)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto extends BaseComment {

  private long id;

  private UserShortDto author;

  private long event;

  @JsonFormat(pattern = Variables.DATE_FORMAT)
  private LocalDateTime created;

  @JsonFormat(pattern = Variables.DATE_FORMAT)
  private LocalDateTime edited;

  private boolean isInitiator;
}