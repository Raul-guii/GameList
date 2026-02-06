
package com.game_list.gamelist.repository;

import com.game_list.gamelist.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{

    // busca role pelo nome 
    Optional<Role> findByName(String name);
}
