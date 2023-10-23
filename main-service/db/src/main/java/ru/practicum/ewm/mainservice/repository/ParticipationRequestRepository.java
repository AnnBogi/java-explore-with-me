package ru.practicum.ewm.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.mainservice.entity.ParticipationRequest;
import ru.practicum.ewm.mainservice.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("select request from ParticipationRequest request join fetch request.event event " +
            "where event.initiator.id = ?1 and event.id = ?2 and request.id in ?3")
    List<ParticipationRequest> findRequests(long userId, long eventId, List<Long> ids);

    List<ParticipationRequest> findAllByRequester(User requester);

    Optional<ParticipationRequest> findByIdAndRequesterId(long requestId, long requesterId);

    @Query("select request from ParticipationRequest request join request.event event " +
            "where event.initiator.id = ?1 and event.id = ?2")
    List<ParticipationRequest> findByInitiatorIdAndEventId(long initiatorId, long eventId);
}