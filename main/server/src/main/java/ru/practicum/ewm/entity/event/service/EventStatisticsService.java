package ru.practicum.ewm.entity.event.service;

import ru.practicum.ewm.MainServerApplication;
import ru.practicum.ewm.entity.event.entity.Event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public interface EventStatisticsService {
    String APP_NAME = MainServerApplication.class.getSimpleName();
    String EVENT_VIEW_URI = "/events/%d";

    void addEventView(Long eventId, String ip, LocalDateTime timestamp);

    Long getEventViews(String start, String end, String uri, Boolean unique);

    default void addEventViews(Iterable<Event> events, String ip, LocalDateTime timestamp) {
        for (Event event : events) {
            addEventView(event.getId(), ip, timestamp);
        }
    }

    default Long getEventViews(Event event, Boolean uniqueViews) {
        return getEventViews(
                LocalDateTime.from(event.getEventDate()).minusYears(1L).toString(),
                LocalDateTime.from(event.getEventDate()).toString(),
                String.format(EVENT_VIEW_URI, event.getId()),
                uniqueViews);
    }

    default Map<Long, Long> getEventViews(Iterable<Event> events, Boolean uniqueViews) {
        Map<Long, Long> eventViews = new HashMap<>();

        for (Event event : events) {
            Long views = getEventViews(event, uniqueViews);
            eventViews.put(event.getId(), views);
        }
        return eventViews;
    }
}
