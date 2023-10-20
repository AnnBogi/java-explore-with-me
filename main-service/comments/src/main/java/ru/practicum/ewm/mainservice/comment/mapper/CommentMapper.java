package ru.practicum.ewm.mainservice.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.mainservice.comment.dto.CommentDto;
import ru.practicum.ewm.mainservice.comment.dto.CommentRequestDto;
import ru.practicum.ewm.mainservice.comment.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mapping(target = "initiator", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "event", ignore = true)
  @Mapping(target = "edited", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "author", ignore = true)
  Comment fromDto(CommentRequestDto commentRequestDto);

  @Mapping(target = "event", source = "event.id")
  CommentDto toDto(Comment comment);
}