
package com.game_list.gamelist.service;

import com.game_list.gamelist.dto.FavoriteDTO;
import com.game_list.gamelist.dto.GameDTO;
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
    
    @Autowired
    private final IgdbClient igdbClient;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           GameRepository gameRepository,
                           IgdbClient igdbClient) 
                           {
                                
                           this.favoriteRepository = favoriteRepository;
                           this.userRepository = userRepository;
                           this.gameRepository = gameRepository;
                           this.igdbClient = igdbClient;
    }
    
    public FavoriteDTO addFavorite(Long user_id, Long externalGameId){
        
        Game game = gameRepository.findByExternalId(externalGameId)
                .orElseGet(() -> {
                    
                    List<GameDTO> result = igdbClient.sendRequest(
                    "fields id, name, cover.url; where id = " + externalGameId + ";"
                    );
                    
                    if (result.isEmpty()){
                        throw new RuntimeException("Game not found in IGDB");
                    }
                    
                    GameDTO apiGame = result.get(0);
                    
                    Game newGame = new Game();
                    newGame.setExternalId(apiGame.getId());
                    newGame.setName(apiGame.getName());
                    newGame.setCoverUrl(apiGame.getCoverUrl() != null ? apiGame.getCover().getUrl() : null);
                    
                    return gameRepository.save(newGame);
              });
        
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setGame(game);
        favoriteRepository.save(fav);
        
        return new FavoriteDTO(
            fav.getId(),
            fav.getUser().getId(),
            fav.getGame().getId());
    }
    
    public List<Favorite> getFavoriteByUser(Long user_id){
        return favoriteRepository.findByUserId(user_id);
    }
    
    public void removeFavorite(Long user_id, Long externalGameId){
        
        Game game = gameRepository.findByExternalId(externalGameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        
        Favorite fav = favoriteRepository.findByUserIdAndGameId(user_id, game.getId())
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        
        favoriteRepository.delete(fav);
                
        
    }
    
    
}
