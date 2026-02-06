
package com.game_list.gamelist.repository;

import com.game_list.gamelist.entity.Profile;
import com.game_list.gamelist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    
    void deleteByUser(User user);
}
