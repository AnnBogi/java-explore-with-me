package ru.practicum.ewm.mainservice.comment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.mainservice.comment.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
  @Query("SELECT comment FROM Comment comment JOIN FETCH comment.author " +
         "WHERE comment.id = ?1 AND comment.author.id = ?2")
  Optional<Comment> findByIdAndAuthorId(long commentId, long userId);
}