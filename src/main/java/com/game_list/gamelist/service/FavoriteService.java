
package com.game_list.gamelist.service;

import com.game_list.gamelist.dto.FavoriteDTO;
import com.game_list.gamelist.entity.Favorite;
import com.game_list.gamelist.entity.Game;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.repository.FavoriteRepository;
import com.game_list.gamelist.repository.GameRepository;
import com.game_list.gamelist.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    
    private final FavoriteRepository favoriteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameRepository gameRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }
    
    public FavoriteDTO addFavorite(Long user_id, Long game_id){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Game game = gameRepository.findById(game_id)
                .orElseThrow(() -> new RuntimeException("Jogo não encotrado"));
        
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setGame(game);
                
        Favorite saved = favoriteRepository.save(favorite);
        
        return new FavoriteDTO(
            saved.getId(),
            saved.getUser().getId(),
            saved.getGame().getId()
        );
    }
    
    public List<Favorite> getFavoriteByUser(Long user_id){
        return favoriteRepository.findByUserId(user_id);
    }
    
    public void removeFavorite(Long favorite_id){
        favoriteRepository.deleteById(favorite_id);
    }
    
    
}
