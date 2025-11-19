
package com.game_list.gamelist.repository;


import com.game_list.gamelist.entity.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long>{
    List<Game> findByNameContainingIgnoreCase(String keyword);
    Optional<Game> findByExternalId(Long externalId);
    
}
