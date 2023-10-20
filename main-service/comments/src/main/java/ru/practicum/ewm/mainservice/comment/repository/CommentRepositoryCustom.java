package ru.practicum.ewm.mainservice.comment.repository;

import ru.practicum.ewm.mainservice.comment.entity.Comment;

import java.util.List;
import java.util.Map;

interface CommentRepositoryCustom {
  List<Comment> getComments(long eventId, int from, int size);

  List<Comment> getCommentsByUserId(long userId, int from, int size);

  Map<Long, Long> countEvent(List<Long> eventIds);
}
