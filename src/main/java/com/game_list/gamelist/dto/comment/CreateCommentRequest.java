package com.game_list.gamelist.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
        @NotBlank(message = "O comentário não pode estar vazio")
        @Size(max = 2000, message = "O comentário deve ter no máximo 2000 caracteres")
        String content
) {
}