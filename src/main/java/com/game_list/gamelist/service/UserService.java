package com.game_list.gamelist.service;

import com.game_list.gamelist.dto.register.RegisterRequest;
import com.game_list.gamelist.entity.Profile;
import com.game_list.gamelist.entity.Role;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.exception.BusinessRuleException;
import com.game_list.gamelist.exception.ConflictException;
import com.game_list.gamelist.exception.ResourceNotFoundException;
import com.game_list.gamelist.repository.FavoriteRepository;
import com.game_list.gamelist.repository.ProfileRepository;
import com.game_list.gamelist.repository.RoleRepository;
import com.game_list.gamelist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final RoleRepository roleRepository;
    private final FavoriteRepository favoriteRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!user.isActive()) {
            throw new DisabledException("Usuário desativado");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().getName())
                .build();
    }

    public User registerAdmin(User user) {
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("Role ADMIN não encontrado"));

        user.setRole(adminRole);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Admin já existe");
        }
        return userRepository.save(user);
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Já existe uma conta com esse e-mail");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role USER não encontrado"));

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(userRole);

        User savedUser = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setName(request.name());
        profileRepository.save(profile);

        return savedUser;
    }

    public Long getIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + username));
    }

    @Transactional
    public Profile getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!user.isActive()) {
            throw new BusinessRuleException("Conta desativada");
        }

        Profile profile = user.getProfile();

        if (profile == null) {
            throw new BusinessRuleException("Usuário sem perfil");
        }

        return profile;
    }

    @Transactional
    public void reactivateAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        user.setActive(true);
    }

    @Transactional
    public void deactivateAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        user.setActive(false);
    }

    @Transactional
    public void deleteAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        favoriteRepository.deleteByUser(user);
        userRepository.delete(user); // cascade em User.profile já remove o Profile associado
    }

    @Transactional
    public void editProfileName(String username, String newName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Profile profile = user.getProfile();

        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        profile.setName(newName);
        profileRepository.save(profile);

        log.info("Nome de perfil atualizado para o usuário {}: {}", username, newName);
    }
}