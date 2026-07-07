package com.game_list.gamelist.service;

import com.game_list.gamelist.dto.GameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final IgdbClient igdbClient;

    public List<GameDTO> getAllGames() {
        String body = "fields id,name,genres.name,rating,summary,cover.url; limit 20;";
        return igdbClient.sendRequest(body);
    }

    public GameDTO getGameById(Long id) {
        String body = "fields id, name, rating, summary, cover.url, genres.name; where id = " + id + ";";
        List<GameDTO> games = igdbClient.sendRequest(body);
        return games.isEmpty() ? null : games.get(0);
    }

    public List<GameDTO> searchGamesByName(String keyword) {
        String safeKeyword = keyword.replace("\"", "\\\"");
        String body = "fields id,name,genres.name,rating,summary,cover.url; search \"" + safeKeyword + "\"; limit 10;";
        return igdbClient.sendRequest(body);
    }

    public List<GameDTO> getGamesByGenre(List<Long> genreIds) {
        return igdbClient.getGamesByGenres(genreIds);
    }
}