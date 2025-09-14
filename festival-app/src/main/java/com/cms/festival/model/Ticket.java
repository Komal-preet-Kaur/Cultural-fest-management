package com.cms.festival.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Participant participant;

    @ManyToOne(optional = false)
    private Event event;

    private String status; // ISSUED, VALID, CANCELLED

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Participant getParticipant() { return participant; }
    public void setParticipant(Participant participant) { this.participant = participant; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


