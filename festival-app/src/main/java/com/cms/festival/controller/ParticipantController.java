package com.cms.festival.controller;

import com.cms.festival.model.Participant;
import com.cms.festival.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/participants")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("participants", participantService.listAll());
        return "participants/list";
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Participant> listApi() { return participantService.listAll(); }

    @PostMapping("/api")
    @ResponseBody
    public Participant register(@Validated @RequestBody Participant participant) { return participantService.register(participant); }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Participant update(@PathVariable Long id, @Validated @RequestBody Participant participant) { return participantService.update(id, participant); }

    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


