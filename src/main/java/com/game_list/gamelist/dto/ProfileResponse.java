package com.game_list.gamelist.dto;

import com.game_list.gamelist.entity.Profile;

public record ProfileResponse(
        Long userId,
        String username,
        String email,
        String name
) {
    public static ProfileResponse from(Profile profile) {
        return new ProfileResponse(
                profile.getUser().getId(),
                profile.getUser().getUsername(),
                profile.getUser().getEmail(),
                profile.getName()
        );
    }
}