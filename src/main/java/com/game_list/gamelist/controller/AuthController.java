package com.game_list.gamelist.controller;

import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.repository.UserRepository;
import com.game_list.gamelist.service.JwtService;
import com.game_list.gamelist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService; // pra register

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        var created = userService.registerUser(user);
        created.setPassword(null);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        log.info("FallbackLogin attempt for username={}", request.getUsername());
        try {
            Optional<User> maybe = userRepository.findByUsername(request.getUsername());
            if (maybe.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas (usuário)");
            }
            User dbUser = maybe.get();
            boolean matches = passwordEncoder.matches(request.getPassword(), dbUser.getPassword());
            log.info("FallbackLogin matches={} for username={}", matches, request.getUsername());
            if (!matches) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas (senha)");
            }
            String token = jwtService.generateToken(dbUser.getUsername());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception ex) {
            log.error("FallbackLogin error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }

    @PostMapping("/debug/check")
    public ResponseEntity<?> debugCheck(@RequestBody User req) {
        Optional<User> maybe = userRepository.findByUsername(req.getUsername());
        if (maybe.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        boolean matches = passwordEncoder.matches(req.getPassword(), maybe.get().getPassword());
        return ResponseEntity.ok(Collections.singletonMap("matches", matches));
    }
}
