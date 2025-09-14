package com.cms.festival.controller;

import com.cms.festival.model.Ticket;
import com.cms.festival.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Ticket> listApi() { return ticketService.listAll(); }

    @PostMapping("/api/issue")
    @ResponseBody
    public Ticket issue(@RequestParam Long participantId, @RequestParam Long eventId) {
        return ticketService.issue(participantId, eventId);
    }

    @PostMapping("/api/{id}/validate")
    @ResponseBody
    public Ticket validate(@PathVariable Long id) { return ticketService.validate(id); }

    @PostMapping("/api/{id}/cancel")
    @ResponseBody
    public Ticket cancel(@PathVariable Long id) { return ticketService.cancel(id); }

    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


