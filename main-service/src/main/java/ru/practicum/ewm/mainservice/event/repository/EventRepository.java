package ru.practicum.ewm.mainservice.event.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends Repository<Event, Long> {

    @Override
    @Query("SELECT e FROM Event e JOIN FETCH e.location WHERE e.id = ?1")
    Optional<Event> findById(Long eventId);

    List<Event> findByCategoryId(long categoryId);

}