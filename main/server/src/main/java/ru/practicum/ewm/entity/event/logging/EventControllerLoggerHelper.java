package ru.practicum.ewm.entity.event.logging;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.ewm.entity.event.dto.request.AddEventRequestDto;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventAdminRequestDto;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventUserRequestDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.participation.dto.request.UpdateEventParticipationStatusRequestDto;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
@SuppressWarnings({"java:S1192", "java:S107"})
public final class EventControllerLoggerHelper {

    public static void addEvent(
            Logger logger,
            AddEventRequestDto eventDto
    ) {
        logger.info("add EVENT["
                        + "title='{}'"
                        + "].",
                eventDto.getTitle());
    }

    public static void getEventDtoPageByParameters(
            Logger logger,
            Integer from,
            Integer size,
            Set<Long> users,
            Set<Event.State> states,
            Set<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd
    ) {
        logger.info("get EVENT_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "user_ids_count={}, "
                        + "states_count={}, "
                        + "categories_count={}, "
                        + "start_time={}, "
                        + "end_time={}"
                        + "] by parameters.",
                from,
                size,
                (users != null) ? users.size() : "ALL",
                (states != null) ? states.size() : "ALL",
                (categories != null) ? categories.size() : "ALL",
                rangeStart,
                rangeEnd);
    }

    public static void getEventById(
            Logger logger,
            Long userId,
            Long eventId
    ) {
        logger.info("get EVENT["
                        + "initiator_id={}, "
                        + "id={}"
                        + "].",
                userId,
                eventId);
    }

    public static void getUserEventDtoPage(
            Logger logger,
            Long userId,
            Integer from,
            Integer size
    ) {
        logger.info("get EVENT_PAGE<DTO>["
                        + "initiator_id={}, "
                        + "from={}, "
                        + "size={}"
                        + "] by initiator.",
                userId,
                from,
                size);
    }

    public static void getEventParticipationRequests(
            Logger logger,
            Long userId,
            Long eventId,
            Integer from,
            Integer size
    ) {
        logger.info("get PARTICIPATION_REQUEST_PAGE<DTO>["
                        + "initiator_id={}, "
                        + "event_id={}, "
                        + "from={}, "
                        + "size={}"
                        + "] by event.",
                userId,
                eventId,
                from,
                size);
    }

    public static void userUpdateEventById(
            Logger logger,
            Long userId,
            Long eventId,
            UpdateEventUserRequestDto eventDto
    ) {
        logger.info("update EVENT["
                        + "initiator_id={}, "
                        + "event_id={}, "
                        + "state_action={}"
                        + "].",
                userId,
                eventId,
                eventDto.getStateAction());
    }

    public static void adminUpdateEventById(
            Logger logger,
            Long eventId,
            UpdateEventAdminRequestDto adminRequest
    ) {
        logger.info("update EVENT["
                        + "id={}, "
                        + "state_action={}"
                        + "].",
                eventId,
                adminRequest.getStateAction());
    }

    public static void updateEventRequestStatus(
            Logger logger,
            Long userId,
            Long eventId,
            UpdateEventParticipationStatusRequestDto requestStatusDto
    ) {
        logger.info("update EVENT_PARTICIPATION_REQUEST["
                        + "initiator_id={}, "
                        + "event_id={}, "
                        + "request_ids_count={}, "
                        + "status='{}'].",
                userId,
                eventId,
                requestStatusDto.getRequestIds().size(),
                requestStatusDto.getStatus());
    }

    public static void getEventById(Logger logger, Long id) {
        logger.info("get EVENT["
                        + "id={}"
                        + "].",
                id);
    }

    public static void getEventDtoPageByParameters(
            Logger logger,
            Integer from,
            Integer size,
            Event.Sort sort,
            String text,
            Set<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable
    ) {
        logger.info("get EVENT_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "sort={}, "
                        + "text='{}', "
                        + "categories_count={}, "
                        + "paid={}, "
                        + "start_time={}, "
                        + "end_time={}, "
                        + "only_available={}"
                        + "] by parameters.",
                from,
                size,
                sort,
                text,
                (categories != null) ? categories.size() : "ALL",
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable);
    }
}
