package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.FavoriteDTO;
import com.game_list.gamelist.service.FavoriteService;
import com.game_list.gamelist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    @GetMapping("/{externalGameId}")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long externalGameId, Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        boolean isFav = favoriteService.isFavorite(userId, externalGameId);
        return ResponseEntity.ok(isFav);
    }

    @GetMapping
    public List<FavoriteDTO> getFavoriteByUser(Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        return favoriteService.getFavoriteDTOByUser(userId);
    }

    @PostMapping("/{gameId}")
    public FavoriteDTO addFavorite(@PathVariable Long gameId, Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        return favoriteService.addFavorite(userId, gameId);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long gameId, Authentication auth) {
        Long userId = userService.getIdByUsername(auth.getName());
        favoriteService.removeFavorite(userId, gameId);
        return ResponseEntity.noContent().build();
    }
}