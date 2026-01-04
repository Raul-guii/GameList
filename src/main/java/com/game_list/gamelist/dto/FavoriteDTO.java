
package com.game_list.gamelist.dto;

import com.game_list.gamelist.entity.Favorite;
import com.game_list.gamelist.entity.Game;
import com.game_list.gamelist.entity.User;

public class FavoriteDTO {
   private Long id;

   private Long user_id;
   
   private Long gameId;
   
   public FavoriteDTO(){}

    public FavoriteDTO(Long id, Long user_id, Long gameId) {
        this.id = id;
        this.user_id = user_id;
        this.gameId = gameId;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

}
