package ru.practicum.ewm.entity.event.service.statistics.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.event.service.statistics.EventStatisticsService;
import ru.practicum.ewm.hit.client.EndpointHitClient;
import ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.hit.dto.response.stats.ViewStatsResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventStatisticsServiceImpl implements EventStatisticsService {
    private final EndpointHitClient endpointHitClient;

    @Override
    public void addEventView(HttpServletRequest request, LocalDateTime timestamp) {
        log.info("add EVENT_VIEW[uri={}, ip={}, timestamp={}].",
                request.getRequestURL(),
                request.getRemoteAddr(),
                timestamp);

        var requestDto = AddEndpointHitRequestDto.toAddEndpointHitRequestDto(request, APP_NAME, timestamp);
        var responseDto = endpointHitClient.postEndpointHit(requestDto);
        log.debug("EVENT_VIEW[app={}, uri={}, ip={}] saved.",
                responseDto.getApp(),
                responseDto.getUri(),
                responseDto.getIp());
    }

    @Override
    public Long getEventViews(
            LocalDateTime start,
            LocalDateTime end,
            String uri,
            Boolean unique
    ) {
        List<ViewStatsResponseDto> stats = endpointHitClient.getStats(
                start,
                end,
                List.of(uri),
                unique);
        return !stats.isEmpty() ? stats.get(0).getHits() : 0;
    }
}
