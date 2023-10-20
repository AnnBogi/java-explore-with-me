package ru.practicum.ewm.mainservice.comment.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainservice.comment.entity.Comment;
import ru.practicum.ewm.mainservice.comment.entity.CommentCount;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Repository
class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
  private final EntityManager entityManager;

  @Override
  public List<Comment> getComments(long eventId, int from, int size) {
    log.info("Find comments with parameters: eventId={}, from={}, size={}", eventId, from, size);

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);

    Root<Comment> commentRoot = criteriaQuery.from(Comment.class);
    commentRoot.fetch("author");

    criteriaQuery.select(commentRoot).where(criteriaBuilder.equal(commentRoot.get("event"), eventId));

    TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery)
                                                  .setFirstResult(from)
                                                  .setMaxResults(size);

    return typedQuery.getResultList();
  }

  @Override
  public List<Comment> getCommentsByUserId(long userId, int from, int size) {
    log.info("Find comments by UserId={}, from={}, size={}", userId, from, size);

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);

    Root<Comment> commentRoot = criteriaQuery.from(Comment.class);
    commentRoot.fetch("author");

    criteriaQuery.select(commentRoot).where(criteriaBuilder.equal(commentRoot.get("author"), userId));

    TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery)
                                                  .setFirstResult(from)
                                                  .setMaxResults(size);

    return typedQuery.getResultList();
  }

  @Override
  public Map<Long, Long> countEvent(List<Long> eventIds) {
    log.info("Find comments count by list Events.");

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CommentCount> criteriaQuery = criteriaBuilder.createQuery(CommentCount.class);

    Root<Comment> commentRoot = criteriaQuery.from(Comment.class);

    Expression<Long> eventExpression = commentRoot.get("event").get("id");
    Predicate eventPredicate = eventExpression.in(eventIds);

    criteriaQuery.multiselect(eventExpression, criteriaBuilder.count(commentRoot));
    criteriaQuery.groupBy(eventExpression);
    criteriaQuery.where(eventPredicate);

    return entityManager.createQuery(criteriaQuery).getResultStream()
                        .collect(Collectors.toMap(CommentCount::getEventId, CommentCount::getCount));

  }
}