package ru.practicum.ewm.entity.event.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventShortResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.logging.EventServiceLoggingHelper;
import ru.practicum.ewm.entity.event.mapper.EventMapper;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;
import ru.practicum.ewm.entity.event.service.EventPublicService;
import ru.practicum.ewm.entity.event.service.EventStatisticsService;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.participation.repository.ParticipationRequestJpaRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventJpaRepository eventRepository;
    private final ParticipationRequestJpaRepository requestRepository;
    private final EventStatisticsService eventStatisticsService;

    @Override
    public EventFullResponseDto getEventById(Long id, HttpServletRequest request) {
        eventRepository.checkEventExistsById(id);
        Event event = eventRepository.getReferenceById(id);
        eventStatisticsService.addEventView(id, request.getRemoteAddr(), LocalDateTime.now());
        EventFullResponseDto eventDto = getEventFullResponseDto(event);
        EventServiceLoggingHelper.eventDtoReturned(log, eventDto);
        return eventDto;
    }

    @Override
    public Iterable<EventShortResponseDto> searchEventsByParameters(
            String text,
            Set<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            Event.Sort sort,
            Integer from,
            Integer size,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(from, size);
        Iterable<Event> events = eventRepository.searchEventsByParameters(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                Event.State.PUBLISHED,
                pageable);

        if (onlyAvailable == Boolean.TRUE) {
            events = getOnlyAvailableEvents(events);
        }

        eventStatisticsService.addEventViews(events, request.getRemoteAddr(), LocalDateTime.now());
        List<EventShortResponseDto> eventDtos = EventMapper.toShortResponseDto(
                events,
                eventStatisticsService.getEventViews(events, false),
                requestRepository.getEventRequestCount(events, Participation.Status.CONFIRMED));

        if (sort != null) {
            eventDtos = sortEvents(sort, eventDtos);
        }

        EventServiceLoggingHelper.eventDtoPageByUserParametersReturned(log, eventDtos, from, size, sort);
        return eventDtos;
    }

    @SuppressWarnings("java:S112")
    private static List<EventShortResponseDto> sortEvents(
            @NonNull Event.Sort sort,
            Iterable<EventShortResponseDto> eventDtos
    ) {
        List<EventShortResponseDto> sortedEventDtos;
        switch (sort) {
            case EVENT_DATE:
                sortedEventDtos = StreamSupport.stream(eventDtos.spliterator(), false)
                        .sorted(Comparator.comparing(EventShortResponseDto::getId).reversed())
                        .collect(Collectors.toList());
                break;
            case VIEWS:
                sortedEventDtos = StreamSupport.stream(eventDtos.spliterator(), false)
                        .sorted(Comparator.comparing(EventShortResponseDto::getViews).reversed())
                        .collect(Collectors.toList());
                break;
            default:
                throw new RuntimeException(String.format("sorting %s not implemented", sort));
        }
        return sortedEventDtos;
    }

    private List<Event> getOnlyAvailableEvents(Iterable<Event> events) {
        List<Event> availableEvents = new ArrayList<>();
        for (Event event : events) {
            Integer confirmedRequests = requestRepository.getEventRequestCount(
                    event.getId(), Participation.Status.CONFIRMED);

            if (confirmedRequests < event.getParticipantLimit()) {
                availableEvents.add(event);
            }
        }
        return availableEvents;
    }

    private EventFullResponseDto getEventFullResponseDto(Event event) {
        return EventMapper.toEventFullResponseDto(
                event,
                eventStatisticsService.getEventViews(event, false),
                requestRepository.getEventRequestCount(event.getId(), Participation.Status.CONFIRMED));
    }
}
