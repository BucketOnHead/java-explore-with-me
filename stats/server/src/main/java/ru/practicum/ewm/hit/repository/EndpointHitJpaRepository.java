package ru.practicum.ewm.hit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.hit.model.EndpointHit;
import ru.practicum.ewm.hit.model.stats.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitJpaRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = ""
            + "SELECT "
            + "  NEW ru.practicum.ewm.hit.model.stats.ViewStats( "
            + "    hits.app, "
            + "    hits.uri, "
            + "    COUNT(hits.ip) "
            + "  ) "
            + "FROM "
            + "  EndpointHit AS hits "
            + "WHERE "
            + "  ?3 IS NULL "
            + "  OR hits.uri IN ?3 "
            + "  AND hits.timestamp BETWEEN ?1 AND ?2 "
            + "GROUP BY "
            + "  hits.app, "
            + "  hits.uri ")
    List<ViewStats> collectEndpointHitStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Pageable pageable);

    @Query(value = ""
            + "SELECT "
            + "  NEW ru.practicum.ewm.hit.model.stats.ViewStats( "
            + "    hits.app, "
            + "    hits.uri, "
            + "    COUNT(DISTINCT hits.ip) "
            + "  ) "
            + "FROM "
            + "  EndpointHit AS hits "
            + "WHERE "
            + "  ?3 IS NULL "
            + "  OR hits.uri IN ?3 "
            + "  AND hits.timestamp BETWEEN ?1 AND ?2 "
            + "GROUP BY "
            + "  hits.app, "
            + "  hits.uri ")
    List<ViewStats> collectUniqueEndpointStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Pageable pageable);
}
