
package com.game_list.gamelist.dto;

import com.game_list.gamelist.entity.Favorite;
import com.game_list.gamelist.entity.Game;
import com.game_list.gamelist.entity.User;

public class FavoriteDTO {
   private Long id;

   private Long user_id;
   
   private Long game_id;
   
   public FavoriteDTO(){}

    public FavoriteDTO(Long id, Long user_id, Long game_id) {
        this.id = id;
        this.user_id = user_id;
        this.game_id = game_id;
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

    public Long getGame_id() {
        return game_id;
    }

    public void setGame_id(Long game_id) {
        this.game_id = game_id;
    }

    



   
}
