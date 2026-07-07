package com.game_list.gamelist.dto.user;

import com.game_list.gamelist.entity.User;

public record UserResponse(
        Long id,
        String username,
        String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
