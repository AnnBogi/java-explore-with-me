package ru.practicum.ewm.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.mainservice.comment.dto.CommentDto;
import ru.practicum.ewm.mainservice.comment.mapper.CommentMapper;
import ru.practicum.ewm.mainservice.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("comments")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentController {
  private final CommentMapper commentMapper;
  private final CommentService commentService;

  @GetMapping
  public List<CommentDto> getComments(@RequestParam @Positive long eventId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @Positive int size) {
    log.info("Get comments for event ID={}, from={}, size={}", eventId, from, size);
    return commentService.getComments(eventId, from, size).stream()
                         .map(commentMapper::toDto)
                         .collect(Collectors.toList());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{commentId}")
  public void deleteComment(@PathVariable @Positive long commentId) {
    log.info("Delete comment by ID={}", commentId);
    commentService.deleteCommentById(commentId);
  }
}