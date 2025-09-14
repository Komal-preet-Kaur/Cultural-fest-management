package com.cms.schedule.controller;

import com.cms.schedule.model.Schedule;
import com.cms.schedule.repository.ScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Schedule> getByEvent(@PathVariable Long eventId) {
        return scheduleRepository.findByEventId(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/event/{eventId}")
    public Schedule upsert(@PathVariable Long eventId, @RequestBody Schedule payload) {
        Schedule s = scheduleRepository.findByEventId(eventId).orElseGet(Schedule::new);
        s.setEventId(eventId);
        s.setLocation(payload.getLocation());
        s.setScheduledAt(payload.getScheduledAt());
        return scheduleRepository.save(s);
    }
}


