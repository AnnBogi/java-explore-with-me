package ru.practicum.ewm.mainservice.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public abstract class BaseComment {
  @NotBlank
  @Size(max = 2048)
  private String comment;
}