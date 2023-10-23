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

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);

    Root<Comment> commentRoot = cq.from(Comment.class);
    commentRoot.fetch(CommentsTable.AUTHOR);

    cq.select(commentRoot)
                 .where(cb.equal(commentRoot.get(CommentsTable.EVENT), eventId));

    TypedQuery<Comment> typedQuery = entityManager.createQuery(cq)
                                                  .setFirstResult(from)
                                                  .setMaxResults(size);

    return typedQuery.getResultList();
  }

  @Override
  public List<Comment> getCommentsByUserId(long userId, int from, int size) {
    log.info("Find comments by UserId={}, from={}, size={}", userId, from, size);

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);

    Root<Comment> commentRoot = cq.from(Comment.class);
    commentRoot.fetch(CommentsTable.AUTHOR);

    cq.select(commentRoot)
                 .where(cb.equal(commentRoot.get(CommentsTable.AUTHOR), userId));

    TypedQuery<Comment> typedQuery = entityManager.createQuery(cq)
                                                  .setFirstResult(from)
                                                  .setMaxResults(size);

    return typedQuery.getResultList();
  }

  @Override
  public Map<Long, Long> countEvent(List<Long> eventIds) {
    log.info("Find comments count by list Events.");

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<CommentCount> cq = cb.createQuery(CommentCount.class);

    Root<Comment> commentRoot = cq.from(Comment.class);

    Expression<Long> eventExpression = commentRoot.get(CommentsTable.EVENT).get(CommentsTable.ID);
    Predicate eventPredicate = eventExpression.in(eventIds);

    cq.multiselect(eventExpression, cb.count(commentRoot));
    cq.groupBy(eventExpression);
    cq.where(eventPredicate);

    return entityManager.createQuery(cq).getResultStream()
                        .collect(Collectors.toMap(CommentCount::getEventId, CommentCount::getCount));

  }
}