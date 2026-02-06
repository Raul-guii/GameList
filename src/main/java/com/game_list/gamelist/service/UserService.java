package com.game_list.gamelist.service;

import com.game_list.gamelist.entity.Profile;
import com.game_list.gamelist.entity.Role;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.repository.FavoriteRepository;
import com.game_list.gamelist.repository.ProfileRepository;
import com.game_list.gamelist.repository.RoleRepository;
import com.game_list.gamelist.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired 
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
            
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        
        if (!user.isActive()){
            throw new DisabledException("Usuário desativado");
        }
        
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().getName())
                .build();
    }

    public User registerAdmin(User user){
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN não encontrado"));
        
        user.setRole(adminRole);
        
        if (userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Admin já existe");
        }
        return userRepository.save(user);
    }
    
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER não encontrado"));
        
        user.setRole(userRole);
        
        User savedUser = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setName(savedUser.getUsername());
        profileRepository.save(profile);
    
        return savedUser;
    }
    
    public Long getIdByUsername(String username){
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
    }
    
    @Transactional
    public Profile getUserProfile(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!user.isActive()){
            throw new IllegalStateException("Conta desativada");
        }
        
        Profile profile = user.getProfile();

        if (profile == null) {
            throw new IllegalStateException("Usuário sem perfil");
        }

        return profile;
    }

    @Transactional
    public void reactivateAccount(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usário não encontrado"));
        user.setActive(true);
    }
    
    @Transactional
    public void deactivateAccount(String username){
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encotrado"));
        
        user.setActive(false);
    }
    
    @Transactional
    public void deleteAccount(String username){
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
                    
        favoriteRepository.deleteByUser(user);
        profileRepository.deleteByUser(user);
        userRepository.delete(user);
    }
    
    @Transactional
    public void editProfileName(String username, String newName){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
        Profile profile = user.getProfile();
        
        if(profile == null){
           profile = new Profile();
           profile.setUser(user);
           user.setProfile(profile);
        }
        
        profile.setName(newName);
        
        profileRepository.save(profile);
        
        System.out.println("Nome novo: " + profile.getName());

    }
}
