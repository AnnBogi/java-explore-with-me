package ru.practicum.ewm.mainservice.event.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.entity.State;
import ru.practicum.ewm.mainservice.repository.AdminEventRequestParameters;
import ru.practicum.ewm.mainservice.repository.EventRequestParameters;
import ru.practicum.ewm.mainservice.repository.Sort;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class EventRepositoryCustom {


    private final EntityManager entityManager;

    public List<Event> findEventsByInitiatorId(long initiatorId, int from, int size) {
        log.info("Finding events by initiatorId={}, from={}, size={}", initiatorId, from, size);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.cq);

        criteria.cq.select(eventRoot)
                   .where(criteria.cb.equal(eventRoot.get(EventTable.INITIATOR), initiatorId));

        TypedQuery<Event> typedQuery = entityManager.createQuery(criteria.cq);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    public Optional<Event> findByIdAndState(long eventId, State state) {
        log.info("Finding event by eventId={}, state={}", eventId, state);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.cq);

        criteria.cq.select(eventRoot)
                   .where(
                        criteria.cb.equal(eventRoot.get(EventTable.ID), eventId),
                        criteria.cb.equal(eventRoot.get(EventTable.STATE), state)
                );

        return entityManager.createQuery(criteria.cq).getResultStream().findFirst();
    }

    public Optional<Event> findByIdAndInitiatorId(long eventId, long userId) {
        log.info("Finding event by eventId={}, userId={}", eventId, userId);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.cq);

        criteria.cq.select(eventRoot)
                   .where(
                        criteria.cb.equal(eventRoot.get(EventTable.ID), eventId),
                        criteria.cb.equal(eventRoot.get(EventTable.INITIATOR), userId)
                );

        return entityManager.createQuery(criteria.cq).getResultStream().findFirst();
    }

    public List<Event> findEventsByParameters(final EventRequestParameters parameters) {
        log.info("Finding events by parameters: {}", parameters);

        Criteria criteria = new Criteria(entityManager);
        final CriteriaQuery<Event> cq = criteria.cq;
        final CriteriaBuilder cb = criteria.cb;

        Root<Event> eventRoot = getEventRootWithFetchAll(cq);
        List<Predicate> predicates = new ArrayList<>();



        predicates.add(cb.equal(eventRoot.get(EventTable.STATE), State.PUBLISHED));

        if (parameters.getText() != null) {
            predicates.add(
                cb.like(
                    cb.lower(eventRoot.get(EventTable.ANNOTATION)),
                    "%" + parameters.getText().toLowerCase() + "%"
                ));
        }

        if (parameters.getCategories() != null && !parameters.getCategories().isEmpty()) {
            predicates.add(cb.in(eventRoot.get(EventTable.CATEGORY).get(EventTable.ID))
                             .value(parameters.getCategories()));
        }

        if (parameters.isPaid()) {
            predicates.add(cb.isTrue(eventRoot.get(EventTable.PAID)));
        }

        if (parameters.getRangeStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(eventRoot.get(EventTable.EVENT_DATE),
                                                   parameters.getRangeStart()));
        }

        if (parameters.getRangeEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(eventRoot.get(EventTable.EVENT_DATE),
                                                parameters.getRangeEnd()));
        }

        if (parameters.isOnlyAvailable()) {
            predicates.add(cb.lessThan(eventRoot.get(EventTable.CONFIRMED_REQUESTS),
                                       eventRoot.get(EventTable.PARTICIPANT_LIMIT)));
        }

        cq.select(eventRoot).where(predicates.toArray(new Predicate[0]));

        if (parameters.getSort() != null) {
            if (parameters.getSort() == Sort.EVENT_DATE) {
                cq.orderBy(cb.asc(eventRoot.get(EventTable.EVENT_DATE)));
            } else if (parameters.getSort() == Sort.VIEWS) {
                cq.orderBy(cb.asc(eventRoot.get((EventTable.VIEWS))));
            }
        }

        return entityManager.createQuery(cq)
                .setFirstResult(parameters.getFrom())
                .setMaxResults(parameters.getSize())
                .getResultList();
    }

    public List<Event> findEventsByParameters(final AdminEventRequestParameters parameters) {
        log.info("Finding events by parameters: {}", parameters);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.cq);
        List<Predicate> predicates = new ArrayList<>();

        if (parameters.getUsers() != null && !parameters.getUsers().isEmpty()) {
            predicates.add(eventRoot.get(EventTable.INITIATOR).get(EventTable.ID)
                                    .in(parameters.getUsers()));
        }

        if (parameters.getStates() != null && !parameters.getStates().isEmpty()) {
            predicates.add(eventRoot.get(EventTable.STATE)
                                    .in(parameters.getStates()));
        }

        if (parameters.getCategories() != null && !parameters.getCategories().isEmpty()) {
            predicates.add(eventRoot.get(EventTable.CATEGORY).get(EventTable.ID)
                                    .in(parameters.getCategories()));
        }

        if (parameters.getRangeStart() != null) {
            predicates.add(criteria.cb.greaterThanOrEqualTo(eventRoot.get(EventTable.EVENT_DATE),
                                                            parameters.getRangeStart()));
        }

        if (parameters.getRangeEnd() != null) {
            predicates.add(criteria.cb.lessThanOrEqualTo(eventRoot.get(EventTable.EVENT_DATE),
                                                         parameters.getRangeEnd()));
        }

        criteria.cq.select(eventRoot).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteria.cq)
                .setFirstResult(parameters.getFrom())
                .setMaxResults(parameters.getSize())
                .getResultList();
    }

    private static class Criteria {
        CriteriaBuilder cb;
        CriteriaQuery<Event> cq;

        Criteria(EntityManager entityManager) {
            cb = entityManager.getCriteriaBuilder();
            cq = cb.createQuery(Event.class);
        }
    }

    private Root<Event> getEventRootWithFetchAll(CriteriaQuery<Event> query) {
        Root<Event> eventRoot = query.from(Event.class);

        eventRoot.fetch(EventTable.INITIATOR);
        eventRoot.fetch(EventTable.CATEGORY);
        eventRoot.fetch(EventTable.LOCATION);
        return eventRoot;
    }
}