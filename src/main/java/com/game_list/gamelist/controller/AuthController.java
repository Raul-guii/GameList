package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.login.LoginRequest;
import com.game_list.gamelist.dto.register.RegisterRequest;
import com.game_list.gamelist.dto.user.UserResponse;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.exception.BusinessRuleException;
import com.game_list.gamelist.repository.UserRepository;
import com.game_list.gamelist.security.JwtService;
import com.game_list.gamelist.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        User created = userService.registerUser(request);
        return UserResponse.from(created);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for email={}", request.email());

        User dbUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!dbUser.isActive()) {
            throw new BusinessRuleException("Conta desativada");
        }

        boolean matches = passwordEncoder.matches(request.password(), dbUser.getPassword());
        if (!matches) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        // O token continua identificando o usuário pelo username — é o
        // identificador interno usado em todo o resto do sistema (rotas,
        // SecurityContext, etc). O e-mail é usado apenas para a etapa de login.
        String token = jwtService.generateToken(dbUser.getUsername());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}