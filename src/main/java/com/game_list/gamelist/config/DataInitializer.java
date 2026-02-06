
package com.game_list.gamelist.config;

import com.game_list.gamelist.entity.Role;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.repository.RoleRepository;
import com.game_list.gamelist.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class DataInitializer {
        @Bean
    // Registra um CommandLineRunner no contexto do Spring, tudo que estiver dentro dele roda automaticamente
    CommandLineRunner initRoles(RoleRepository roleRepository,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder){
        return args -> {

            System.out.println("Data executando!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            long users = userRepository.count();
            System.out.println("total de usuarios" + users);

            // Cria as roles no banco
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            // Cria um admin padrao  (só uma vez)
            String adminEmail = "admin@admin.com";

            if (!userRepository.existsByEmail(adminEmail)){
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(adminRole);
                admin.setActive(true);
                userRepository.save(admin);
                System.out.println("Admin padrão criado");                
            }
        };            
     }
}
