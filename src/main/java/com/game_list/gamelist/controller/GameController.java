package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.GameDTO;
import com.game_list.gamelist.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public List<GameDTO> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public GameDTO getGame(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @GetMapping("/search")
    public List<GameDTO> searchGames(@RequestParam String keyword) {
        return gameService.searchGamesByName(keyword);
    }

    @GetMapping("/genres")
    public List<GameDTO> getGamesByGenres(@RequestParam List<Long> genres) {
        return gameService.getGamesByGenre(genres);
    }
}