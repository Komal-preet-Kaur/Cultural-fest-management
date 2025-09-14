package com.cms.festival.log;

import org.springframework.stereotype.Service;

@Service
public class FestivalLogService {
    private final FestivalLogRepository repository;

    public FestivalLogService(FestivalLogRepository repository) {
        this.repository = repository;
    }

    public void log(String action, String role, String details) {
        repository.save(new FestivalLog(action, role, details));
    }
}


