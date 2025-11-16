
package com.game_list.gamelist.repository;

import com.game_list.gamelist.entity.Favorite;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long user_id);
}
