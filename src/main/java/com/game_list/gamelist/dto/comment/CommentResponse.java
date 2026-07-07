package com.game_list.gamelist.dto.comment;

import com.game_list.gamelist.entity.Comment;

import java.time.Instant;

public record CommentResponse(
        Long id,
        String username,
        Long gameExternalId,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getGame().getExternalId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
