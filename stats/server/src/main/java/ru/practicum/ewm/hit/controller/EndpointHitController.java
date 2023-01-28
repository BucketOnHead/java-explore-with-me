package ru.practicum.ewm.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.hit.dto.response.EndpointHitResponseDto;
import ru.practicum.ewm.hit.dto.response.stats.ViewStatsResponseDto;
import ru.practicum.ewm.hit.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitResponseDto addEndpointHit(@RequestBody AddEndpointHitRequestDto endpointHitDto) {
        log.info("Запрос на сохранение просмотра: ENDPOINT_HIT<DTO>[URI='{}', IP={}].",
                endpointHitDto.getUri(),
                endpointHitDto.getIp());

        return endpointHitService.addEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsResponseDto> getStats(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос статистики {}просмотров по {} uri-адресам.",
                (unique == Boolean.TRUE) ? "уникальных " : "",
                (uris != null) ? uris.size() : "всем");

        return endpointHitService.getStats(start, end, uris, unique, from, size);
    }
}
