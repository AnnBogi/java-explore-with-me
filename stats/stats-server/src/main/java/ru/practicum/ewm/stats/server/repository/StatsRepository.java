package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.server.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT e.uri, count(DISTINCT e.ip) as c FROM EndpointHit e " +
            "WHERE e.timestamp >= :start AND e.timestamp <= :end " +
            "AND (COALESCE(:uris, NULL) IS NULL OR e.uri in (:uris)) " +
            "GROUP BY e.uri ORDER BY c DESC")
    List<Object[]> countDistinctIpByUris(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT e.uri, count(e.ip) as c FROM EndpointHit e " +
            "WHERE e.timestamp >= :start AND e.timestamp <= :end " +
            "AND (COALESCE(:uris, NULL) IS NULL OR e.uri in (:uris)) " +
            "GROUP BY e.uri ORDER BY c DESC")
    List<Object[]> countIpByUris(List<String> uris, LocalDateTime start, LocalDateTime end);
}