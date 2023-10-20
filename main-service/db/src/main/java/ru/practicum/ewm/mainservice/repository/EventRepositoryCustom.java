package ru.practicum.ewm.mainservice.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.entity.State;

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
    private static final String ID = "id";
    private static final String INITIATOR = "initiator";
    private static final String STATE = "state";
    private static final String CATEGORY = "category";
    private static final String EVENT_DATE = "eventDate";

    private final EntityManager entityManager;

    public List<Event> findEventsByInitiatorId(long initiatorId, int from, int size) {
        log.info("Finding events by initiatorId={}, from={}, size={}", initiatorId, from, size);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.query);

        criteria.query.select(eventRoot)
                .where(criteria.builder.equal(eventRoot.get(INITIATOR), initiatorId));

        TypedQuery<Event> typedQuery = entityManager.createQuery(criteria.query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    public Optional<Event> findByIdAndState(long eventId, State state) {
        log.info("Finding event by eventId={}, state={}", eventId, state);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.query);

        criteria.query.select(eventRoot)
                .where(
                        criteria.builder.equal(eventRoot.get(ID), eventId),
                        criteria.builder.equal(eventRoot.get(STATE), state)
                );

        return entityManager.createQuery(criteria.query).getResultStream().findFirst();
    }

    public Optional<Event> findByIdAndInitiatorId(long eventId, long userId) {
        log.info("Finding event by eventId={}, userId={}", eventId, userId);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.query);

        criteria.query.select(eventRoot)
                .where(
                        criteria.builder.equal(eventRoot.get(ID), eventId),
                        criteria.builder.equal(eventRoot.get(INITIATOR), userId)
                );

        return entityManager.createQuery(criteria.query).getResultStream().findFirst();
    }

    public List<Event> findEventsByParameters(final EventRequestParameters parameters) {
        log.info("Finding events by parameters: {}", parameters);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.query);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteria.builder.equal(eventRoot.get(STATE), State.PUBLISHED));

        if (parameters.getText() != null) {
            predicates.add(
                criteria.builder.like(
                    criteria.builder.lower(eventRoot.get("annotation")),
                    "%" + parameters.getText().toLowerCase() + "%"
                ));
        }

        if (parameters.getCategories() != null && !parameters.getCategories().isEmpty()) {
            predicates.add(criteria.builder.in(eventRoot.get(CATEGORY).get(ID))
                                           .value(parameters.getCategories()));
        }

        if (parameters.isPaid()) {
            predicates.add(criteria.builder.isTrue(eventRoot.get("paid")));
        }

        if (parameters.getRangeStart() != null) {
            predicates.add(criteria.builder.greaterThanOrEqualTo(eventRoot.get(EVENT_DATE),
                    parameters.getRangeStart()));
        }

        if (parameters.getRangeEnd() != null) {
            predicates.add(criteria.builder.lessThanOrEqualTo(eventRoot.get(EVENT_DATE),
                    parameters.getRangeEnd()));
        }

        if (parameters.isOnlyAvailable()) {
            predicates.add(criteria.builder.lessThan(eventRoot.get("confirmedRequests"),
                    eventRoot.get("participantLimit")));
        }

        criteria.query.select(eventRoot).where(predicates.toArray(new Predicate[0]));

        if (parameters.getSort() != null) {
            if (parameters.getSort() == Sort.EVENT_DATE) {
                criteria.query.orderBy(criteria.builder.asc(eventRoot.get(EVENT_DATE)));
            } else if (parameters.getSort() == Sort.VIEWS) {
                criteria.query.orderBy(criteria.builder.asc(eventRoot.get("views")));
            }
        }

        return entityManager.createQuery(criteria.query)
                .setFirstResult(parameters.getFrom())
                .setMaxResults(parameters.getSize())
                .getResultList();
    }

    public List<Event> findEventsByParameters(final AdminEventRequestParameters parameters) {
        log.info("Finding events by parameters: {}", parameters);

        Criteria criteria = new Criteria(entityManager);
        Root<Event> eventRoot = getEventRootWithFetchAll(criteria.query);
        List<Predicate> predicates = new ArrayList<>();

        if (parameters.getUsers() != null && !parameters.getUsers().isEmpty()) {
            predicates.add(eventRoot.get(INITIATOR).get(ID)
                                    .in(parameters.getUsers()));
        }

        if (parameters.getStates() != null && !parameters.getStates().isEmpty()) {
            predicates.add(eventRoot.get(STATE)
                                    .in(parameters.getStates()));
        }

        if (parameters.getCategories() != null && !parameters.getCategories().isEmpty()) {
            predicates.add(eventRoot.get(CATEGORY).get(ID)
                                    .in(parameters.getCategories()));
        }

        if (parameters.getRangeStart() != null) {
            predicates.add(criteria.builder.greaterThanOrEqualTo(eventRoot.get(EVENT_DATE),
                    parameters.getRangeStart()));
        }

        if (parameters.getRangeEnd() != null) {
            predicates.add(criteria.builder.lessThanOrEqualTo(eventRoot.get(EVENT_DATE),
                    parameters.getRangeEnd()));
        }

        criteria.query.select(eventRoot).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteria.query)
                .setFirstResult(parameters.getFrom())
                .setMaxResults(parameters.getSize())
                .getResultList();
    }

    private static class Criteria {
        CriteriaBuilder builder;
        CriteriaQuery<Event> query;

        Criteria(EntityManager entityManager) {
            builder = entityManager.getCriteriaBuilder();
            query = builder.createQuery(Event.class);
        }
    }

    private Root<Event> getEventRootWithFetchAll(CriteriaQuery<Event> query) {
        Root<Event> eventRoot = query.from(Event.class);

        eventRoot.fetch(INITIATOR);
        eventRoot.fetch(CATEGORY);
        eventRoot.fetch("location");
        return eventRoot;
    }
}