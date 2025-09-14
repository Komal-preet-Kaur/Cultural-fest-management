package com.cms.festival.repository;

import com.cms.festival.model.Event;
import com.cms.festival.model.Participant;
import com.cms.festival.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByParticipantIdAndEventId(Long participantId, Long eventId);

    int countByEventId(Long eventId);

    @Query("SELECT DISTINCT t.participant FROM Ticket t WHERE t.event.id = :eventId")
    List<Participant> findParticipantsByEventId(@Param("eventId") Long eventId);

    @Query("SELECT DISTINCT t.event FROM Ticket t WHERE t.participant.id = :participantId")
    List<Event> findEventsByParticipantId(@Param("participantId") Long participantId);

    List<Ticket> findByEventId(Long eventId);
}
