package com.cms.festival.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ScheduleClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${scheduling.service.base-url}")
    private String baseUrl;

    public Map getByEvent(Long eventId) {
        return restTemplate.getForObject(baseUrl + "/api/schedules/event/" + eventId, Map.class);
    }

    public Map upsert(Long eventId, LocalDateTime scheduledAt, String location) {
        Map<String, Object> payload = Map.of(
                "eventId", eventId,
                "scheduledAt", scheduledAt.toString(),
                "location", location
        );
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/api/schedules/event/" + eventId,
                HttpMethod.PUT, new HttpEntity<>(payload), Map.class);
        return response.getBody();
    }
}


