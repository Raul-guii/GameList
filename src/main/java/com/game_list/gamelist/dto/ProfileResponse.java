
package com.game_list.gamelist.dto;

import com.game_list.gamelist.entity.Profile;

public record ProfileResponse(String name) {
        public static ProfileResponse from(Profile profile) {
            return new ProfileResponse(profile.getName());
        }
    }


