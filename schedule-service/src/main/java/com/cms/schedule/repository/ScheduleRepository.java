package com.cms.schedule.repository;

import com.cms.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByEventId(Long eventId);
}


