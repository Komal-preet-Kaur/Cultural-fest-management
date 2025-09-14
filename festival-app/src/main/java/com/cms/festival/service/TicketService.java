package com.cms.festival.service;

import com.cms.festival.model.Event;
import com.cms.festival.model.Participant;
import com.cms.festival.model.Ticket;
import com.cms.festival.repository.EventRepository;
import com.cms.festival.repository.ParticipantRepository;
import com.cms.festival.repository.TicketRepository;
import org.springframework.stereotype.Service;
import com.cms.festival.log.FestivalLogService;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final FestivalLogService logService;

    public TicketService(TicketRepository ticketRepository,
                         ParticipantRepository participantRepository,
                         EventRepository eventRepository,
                         FestivalLogService logService) {
        this.ticketRepository = ticketRepository;
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
        this.logService = logService;
    }

    public List<Ticket> listAll() { return ticketRepository.findAll(); }

    public Ticket issue(Long participantId, Long eventId) {
        Participant participant = participantRepository.findById(participantId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        Ticket ticket = new Ticket();
        ticket.setParticipant(participant);
        ticket.setEvent(event);
        ticket.setStatus("ISSUED");
        Ticket saved = ticketRepository.save(ticket);
        logService.log("TICKET_ISSUED", "ORGANIZER", "Ticket id=" + saved.getId());
        return saved;
    }

    public Ticket validate(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setStatus("VALID");
        Ticket saved = ticketRepository.save(ticket);
        logService.log("TICKET_VALIDATED", "ORGANIZER", "Ticket id=" + saved.getId());
        return saved;
    }

    public Ticket cancel(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setStatus("CANCELLED");
        Ticket saved = ticketRepository.save(ticket);
        logService.log("TICKET_CANCELLED", "ORGANIZER", "Ticket id=" + saved.getId());
        return saved;
    }

    public void delete(Long id) { ticketRepository.deleteById(id); logService.log("TICKET_DELETED", "ORGANIZER", "Ticket id=" + id); }

    public boolean isParticipating(Long participantId, Long eventId) {
        return ticketRepository.existsByParticipantIdAndEventId(participantId, eventId);
    }

    public int getParticipantCount(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }

    public List<Participant> getEventParticipants(Long eventId) {
        return ticketRepository.findParticipantsByEventId(eventId);
    }

    public List<Event> getParticipantEvents(Long participantId) {
        return ticketRepository.findEventsByParticipantId(participantId);
    }
}


