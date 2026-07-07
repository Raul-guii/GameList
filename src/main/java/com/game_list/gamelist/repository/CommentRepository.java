package com.game_list.gamelist.repository;

import com.game_list.gamelist.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByGame_ExternalIdOrderByCreatedAtDesc(Long externalId);

    List<Comment> findByUser_Username(String username);

    Optional<Comment> findByIdAndUser_Username(Long id, String username);
}