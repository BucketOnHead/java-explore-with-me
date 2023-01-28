package ru.practicum.ewm.hit.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.hit.dto.response.EndpointHitResponseDto;
import ru.practicum.ewm.hit.dto.response.stats.ViewStatsResponseDto;

import java.util.List;

public class EndpointHitClient {
    private final WebClient webClient;

    public EndpointHitClient(String serverUrl) {
        this.webClient = WebClient.create(serverUrl);
    }

    public EndpointHitResponseDto postEndpointHit(AddEndpointHitRequestDto endpointHitDto) {
        return webClient.post()
                .uri("/hit")
                .bodyValue(endpointHitDto)
                .retrieve()
                .bodyToMono(EndpointHitResponseDto.class)
                .block();
    }

    public List<ViewStatsResponseDto> getStats(String start,
                                               String end,
                                               List<String> uris,
                                               Boolean unique,
                                               Integer from,
                                               Integer size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStatsResponseDto.class)
                .collectList()
                .block();
    }
}
