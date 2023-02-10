package ru.practicum.ewm.entity.participation.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.participation.entity.Participation.Status;
import ru.practicum.ewm.entity.participation.exception.ParticipationRequestNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public interface ParticipationRequestJpaRepository extends JpaRepository<Participation, Long> {

    @Query(""
            + "SELECT "
            + "  COUNT(*) "
            + "FROM "
            + "  Participation AS requests "
            + "WHERE "
            + "  requests.event.id = ?1 "
            + "AND "
            + " requests.status = ?2 ")
    Integer getEventRequestCount(Long eventId, Status requestStatus);

    List<Participation> findAllByRequesterId(Long userId);

    Page<Participation> findAllByEventInitiatorIdAndEventId(Long userId, Long eventId, Pageable pageable);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    default void checkParticipationExistsById(@NonNull Long requestId) {
        if (!existsById(requestId)) {
            throw ParticipationRequestNotFoundException.fromRequestId(requestId);
        }
    }

    default Map<Long, Integer> getEventRequestCount(Set<Long> eventIds, Status requestStatus) {
        Map<Long, Integer> eventCountRequests = new HashMap<>();

        for (Long eventId : eventIds) {
            Integer countRequests = getEventRequestCount(eventId, requestStatus);
            eventCountRequests.put(eventId, countRequests);
        }

        return eventCountRequests;
    }

    default Map<Long, Integer> getEventRequestCount(Iterable<Event> events, Status requestStatus) {
        Set<Long> eventIds = StreamSupport.stream(events.spliterator(), false)
                .map(Event::getId)
                .collect(Collectors.toSet());

        return getEventRequestCount(eventIds, requestStatus);
    }
}
