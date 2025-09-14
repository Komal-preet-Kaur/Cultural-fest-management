package com.cms.festival.controller;

import com.cms.festival.model.Event;
import com.cms.festival.model.Participant;
import com.cms.festival.model.Ticket;
import com.cms.festival.service.EventService;
import com.cms.festival.service.TicketService;
import com.cms.festival.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final TicketService ticketService;
    private final ParticipantService participantService;

    public EventController(EventService eventService, TicketService ticketService, 
                          ParticipantService participantService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.participantService = participantService;
    }

    @GetMapping("/cards")
    public String cards(Model model) {
        model.addAttribute("events", eventService.listAll());
        return "events/cards";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("events", eventService.listAll());
        return "events/list";
    }

    @GetMapping("/new")
    public String newForm() { return "events/new"; }

    @GetMapping("/my-participations")
    public String myParticipations() { return "events/my-participations"; }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        try {
            Event event = eventService.getById(id);
            model.addAttribute("event", event);
            return "events/details";
        } catch (Exception e) {
            return "events/details"; // Will show error state
        }
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Event> listApi() { return eventService.listAll(); }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getEventApi(@PathVariable Long id) {
        try {
            Event event = eventService.getById(id);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Event not found"));
        }
    }

    @PostMapping("/api")
    @ResponseBody
    public Event create(@Validated @RequestBody Event event) { return eventService.create(event); }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Event update(@PathVariable Long id, @Validated @RequestBody Event event) { return eventService.update(id, event); }

    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/{eventId}/participate")
    @ResponseBody
    public ResponseEntity<?> participate(@PathVariable Long eventId, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(Map.of("error", "Please login first"));
            }

            String userEmail = authentication.getName();
            Participant participant = participantService.findByEmail(userEmail);
            
            if (participant == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Participant not found"));
            }

            Event event = eventService.getById(eventId);
            
            // Check if already participating
            if (ticketService.isParticipating(participant.getId(), eventId)) {
                return ResponseEntity.status(409).body(Map.of("error", "Already participating in this event"));
            }

            // Check capacity if event has one
            if (event.getCapacity() != null) {
                int currentParticipants = ticketService.getParticipantCount(eventId);
                if (currentParticipants >= event.getCapacity()) {
                    return ResponseEntity.status(409).body(Map.of("error", "Event is full"));
                }
            }

            Ticket ticket = ticketService.issue(participant.getId(), eventId);
            return ResponseEntity.ok(Map.of(
                "message", "Successfully joined the event!",
                "ticketId", ticket.getId(),
                "eventName", event.getName()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to participate: " + e.getMessage()));
        }
    }

    @GetMapping("/api/{eventId}/participants")
    @ResponseBody
    public ResponseEntity<?> getEventParticipants(@PathVariable Long eventId) {
        try {
            List<Participant> participants = ticketService.getEventParticipants(eventId);
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get participants: " + e.getMessage()));
        }
    }

    @GetMapping("/api/my-participations")
    @ResponseBody
    public ResponseEntity<?> getMyParticipations(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(Map.of("error", "Please login first"));
            }

            String userEmail = authentication.getName();
            Participant participant = participantService.findByEmail(userEmail);
            
            if (participant == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Participant not found"));
            }

            List<Event> myEvents = ticketService.getParticipantEvents(participant.getId());
            return ResponseEntity.ok(myEvents);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get participations: " + e.getMessage()));
        }
    }
}


