package com.cms.festival.service;

import com.cms.festival.model.Participant;
import com.cms.festival.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import com.cms.festival.log.FestivalLogService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final FestivalLogService logService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ParticipantService(ParticipantRepository participantRepository, FestivalLogService logService) {
        this.participantRepository = participantRepository;
        this.logService = logService;
    }

    public List<Participant> listAll() { return participantRepository.findAll(); }

    public Participant getById(Long id) { return participantRepository.findById(id).orElseThrow(); }

    public Participant register(Participant participant) {
        participant.setPassword(encoder.encode(participant.getPassword()));
        Participant saved = participantRepository.save(participant);
        logService.log("PARTICIPANT_REGISTERED", saved.getRole(), "Participant id=" + saved.getId());
        return saved;
    }

    public Participant update(Long id, Participant data) {
        Participant existing = getById(id);
        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        existing.setRole(data.getRole());
        Participant saved = participantRepository.save(existing);
        logService.log("PARTICIPANT_UPDATED", saved.getRole(), "Participant id=" + saved.getId());
        return saved;
    }

    public void delete(Long id) {
        participantRepository.deleteById(id);
        logService.log("PARTICIPANT_DELETED", "ORGANIZER", "Participant id=" + id);
    }

    public Participant findByEmail(String email) {
        return participantRepository.findByEmail(email).orElse(null);
    }
}


