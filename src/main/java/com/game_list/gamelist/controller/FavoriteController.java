
package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.FavoriteDTO;
import com.game_list.gamelist.entity.Favorite;
import com.game_list.gamelist.repository.UserRepository;
import com.game_list.gamelist.service.FavoriteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }
    
    @GetMapping("/{user_id}")
    public List<Favorite> getFavoriteByUser(@PathVariable Long user_id){
        return favoriteService.getFavoriteByUser(user_id);
    }
    
    @PostMapping("/{user_id}/{game_id}")
    public FavoriteDTO addFavorite(@PathVariable Long user_id, @PathVariable Long game_id){
        return favoriteService.addFavorite(user_id, game_id);
    }
    
    @DeleteMapping("/{id}")
    public void removeFavorite(@PathVariable Long id){
       favoriteService.removeFavorite(id);
    }
    
    
    
}
