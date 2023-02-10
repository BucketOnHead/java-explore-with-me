package ru.practicum.ewm.hit.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AddEndpointHitRequestDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

    public static AddEndpointHitRequestDto toAddEndpointHitRequestDto(
            String app,
            String uri,
            String ip,
            LocalDateTime timestamp
    ) {
        AddEndpointHitRequestDto endpointHitDto = new AddEndpointHitRequestDto();

        endpointHitDto.setApp(app);
        endpointHitDto.setUri(uri);
        endpointHitDto.setIp(ip);
        endpointHitDto.setTimestamp(timestamp);

        return endpointHitDto;
    }
}
