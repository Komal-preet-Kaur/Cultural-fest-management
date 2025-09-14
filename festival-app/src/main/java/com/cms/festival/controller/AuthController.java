package com.cms.festival.controller;

import com.cms.festival.model.Participant;
import com.cms.festival.repository.ParticipantRepository;
import com.cms.festival.security.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final ParticipantRepository participantRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(ParticipantRepository participantRepository, JwtUtil jwtUtil) {
        this.participantRepository = participantRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public String loginPage() { return "auth/login"; }

    @GetMapping("/register")
    public String registerPage() { return "auth/register"; }

    // home mappings moved to HomeController

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Participant p) {
        // Basic validation
        if (p.getEmail() == null || p.getEmail().isBlank() || p.getPassword() == null || p.getPassword().isBlank() || p.getName() == null || p.getName().isBlank() || p.getRole() == null || p.getRole().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
        }

        // Pre-check duplicate email to return a friendly error instead of 500
        if (participantRepository.findByEmail(p.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "Email already registered"));
        }

        try {
            p.setPassword(encoder.encode(p.getPassword()));
            Participant saved = participantRepository.save(p);
            return ResponseEntity.ok(Map.of("id", saved.getId(), "email", saved.getEmail()));
        } catch (DataIntegrityViolationException e) {
            // Race-condition fallback if unique constraint is hit
            return ResponseEntity.status(409).body(Map.of("error", "Email already registered"));
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        Participant user = participantRepository.findByEmail(email).orElse(null);
        if (user == null || !encoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of("token", token, "role", user.getRole()));
    }
}


