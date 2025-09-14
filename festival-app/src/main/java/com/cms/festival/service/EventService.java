package com.cms.festival.service;

import com.cms.festival.model.Event;
import com.cms.festival.model.Ticket;
import com.cms.festival.repository.EventRepository;
import com.cms.festival.repository.TicketRepository;
import org.springframework.stereotype.Service;
import com.cms.festival.log.FestivalLogService;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final FestivalLogService logService;

    public EventService(EventRepository eventRepository, TicketRepository ticketRepository,
            FestivalLogService logService) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.logService = logService;
    }

    public List<Event> listAll() {
        return eventRepository.findAll();
    }

    public Event getById(Long id) {
        return eventRepository.findById(id).orElseThrow();
    }

    public Event create(Event event) {
        Event saved = eventRepository.save(event);
        logService.log("EVENT_CREATED", "ORGANIZER", "Event id=" + saved.getId());
        return saved;
    }

    public Event update(Long id, Event updated) {
        Event existing = getById(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setScheduledAt(updated.getScheduledAt());
        existing.setLocation(updated.getLocation());
        existing.setCapacity(updated.getCapacity());
        Event saved = eventRepository.save(existing);
        logService.log("EVENT_UPDATED", "ORGANIZER", "Event id=" + saved.getId());
        return saved;
    }

    public void delete(Long id) {
        // First delete all tickets associated with this event
        List<Ticket> tickets = ticketRepository.findByEventId(id);
        if (tickets != null && !tickets.isEmpty()) {
            ticketRepository.deleteAll(tickets);
            logService.log("TICKETS_DELETED", "ORGANIZER", "Deleted " + tickets.size() + " tickets for event id=" + id);
        }

        // Then delete the event
        eventRepository.deleteById(id);
        logService.log("EVENT_DELETED", "ORGANIZER", "Event id=" + id);
    }
}
