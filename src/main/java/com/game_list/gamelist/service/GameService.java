package com.game_list.gamelist.service;

import com.game_list.gamelist.dto.GameDTO;
import com.game_list.gamelist.entity.Game;
import com.game_list.gamelist.entity.Genre;
import com.game_list.gamelist.repository.GameRepository;
import com.game_list.gamelist.repository.GenreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final IgdbClient igdbClient;
    private final GameRepository gameRepository;
    
    @Autowired
    private GenreRepository  genreRepository;
    
    public GameService(IgdbClient igdbClient, GameRepository gameRepository) {
        this.igdbClient = igdbClient;
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
    }

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
        String body = "fields id,name,genres.name,rating,summary,cover.url; search \"" + keyword + "\"; limit 10;";
        return igdbClient.sendRequest(body);
    }
    
    public List<GameDTO> getGamesByGenre(List<Long> genreIds) {
        return igdbClient.getGamesByGenres(genreIds);
    }
    
   /* public void syncGamesFromApi(){
        String body = "fields id,name,cover.url,genres;\n" +
                      "limit 50;";
        
        List<GameDTO> gamesFromApi = igdbClient.sendRequest(body);
        
        for (GameDTO dto : gamesFromApi){
            Game game = new Game();
            game.setId(dto.getId());
            game.setName(dto.getName());
            game.setRating(dto.getRating());
            game.setSummary(dto.getSummary());
            
            List<Genre> genreEntities = new ArrayList<>();
            if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
                genreEntities = genreRepository.findAllById(dto.getGenres());
            }   
            game.setGenres(genreEntities);
        
            game.setCoverUrl(dto.getCover() != null ? dto.getCover().getUrl() : null);
            
            gameRepository.save(game);
                    
        }
        System.out.println("Sync concluído! " + gamesFromApi.size() + " jogos sincronizados!");
        
    }*/

}
