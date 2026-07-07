package com.game_list.gamelist.config;

import com.game_list.gamelist.entity.Role;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.repository.RoleRepository;
import com.game_list.gamelist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("dev")
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${ADMIN_DEFAULT_EMAIL:admin@admin.com}")
    private String adminEmail;

    @Value("${ADMIN_DEFAULT_USERNAME:admin}")
    private String adminUsername;

    @Value("${ADMIN_DEFAULT_PASSWORD:admin123}")
    private String adminPassword;

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {

            log.info("Executando DataInitializer (profile dev)");
            log.info("Total de usuários cadastrados: {}", userRepository.count());

            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(adminRole);
                admin.setActive(true);
                userRepository.save(admin);
                log.info("Admin padrão criado: {}", adminUsername);
            }
        };
    }
}