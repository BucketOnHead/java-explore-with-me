package ru.practicum.ewm.entity.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.event.service.EventStatisticsService;
import ru.practicum.ewm.hit.client.EndpointHitClient;
import ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.hit.dto.response.stats.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto.toAddEndpointHitRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventStatisticsServiceImpl implements EventStatisticsService {
    private final EndpointHitClient endpointHitClient;

    @Override
    public void addEventView(Long eventId, String ip, LocalDateTime timestamp) {
        log.info("add EVENT_VIEW[event_id={}, ip={}, timestamp={}].", eventId, ip, timestamp);
        var requestDto = getEndpointHitRequestDto(eventId, ip, timestamp);
        var responseDto = endpointHitClient.postEndpointHit(requestDto);
        log.debug("EVENT_VIEW[app={}, uri={}, ip={}] saved.",
                responseDto.getApp(),
                responseDto.getUri(),
                responseDto.getIp());
    }

    @Override
    public Long getEventViews(String start, String end, String uri, Boolean unique) {
        List<ViewStatsResponseDto> stats = endpointHitClient.getStats(start, end, List.of(uri), unique);
        return !stats.isEmpty() ? stats.get(0).getHits() : 0;
    }

    private static AddEndpointHitRequestDto getEndpointHitRequestDto(Long eventId, String ip, LocalDateTime timestamp) {
        String uri = String.format(EVENT_VIEW_URI, eventId);
        return toAddEndpointHitRequestDto(APP_NAME, uri, ip, timestamp);
    }
}
