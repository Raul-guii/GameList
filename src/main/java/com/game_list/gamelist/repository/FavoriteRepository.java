
package com.game_list.gamelist.repository;

import com.game_list.gamelist.entity.Favorite;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long user_id);
    Optional<Favorite> findByUserIdAndGameId(Long user_id, Long gameId);
    Optional<Favorite> findByUserIdAndGame_ExternalId(Long user_id, Long externalId);

    boolean existsByUserIdAndGameId(Long user_id, Long id);
}
