
package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.FavoriteDTO;
import com.game_list.gamelist.entity.Favorite;
import com.game_list.gamelist.repository.UserRepository;
import com.game_list.gamelist.service.FavoriteService;
import com.game_list.gamelist.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins = {"http://localhost:4201", "http://127.0.0.1:4201"})
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @Autowired
    private final UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    public FavoriteController(FavoriteService favoriteService, UserRepository userRepository, UserService userService) {
        this.favoriteService = favoriteService;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    
    @GetMapping
    public List<Favorite> getFavoriteByUser(Authentication auth){
        Long user_id = userService.getIdByUsername(auth.getName());
        return favoriteService.getFavoriteByUser(user_id);
    }
    
    @PostMapping("/{game_id}")
    public FavoriteDTO addFavorite(@PathVariable Long game_id, Authentication auth){
       Long user_id = userService.getIdByUsername(auth.getName());
       return favoriteService.addFavorite(user_id, game_id);
    }
    
    @DeleteMapping("/{game_id}")
    public ResponseEntity<?> removeFavoritte(@PathVariable Long game_id, Authentication auth){
        Long user_id = userService.getIdByUsername(auth.getName());
        favoriteService.removeFavorite(user_id, game_id);
        return ResponseEntity.ok().build();
    }
    
    
    
}
