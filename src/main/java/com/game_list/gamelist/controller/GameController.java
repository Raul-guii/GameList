
package com.game_list.gamelist.controller;



import com.game_list.gamelist.dto.GameDTO;
import com.game_list.gamelist.service.GameService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/games")
public class GameController {
    
    private final GameService gameService;
    
    public GameController(GameService gameService){
        this.gameService = gameService;
    }
    
    @GetMapping
    public List<GameDTO> getAllGames(){
        return gameService.getAllGames();
    }
    
    @GetMapping("/{id}")
    public GameDTO getGame(@PathVariable Long id){
        return gameService.getGameById(id);
    }
    
    @GetMapping("/search")
    public List<GameDTO> getGameById(@RequestParam String keyword){
        return gameService.searchGamesByName(keyword);
    }
    
    @GetMapping("/genres")
    public List<GameDTO> getGamesByGenres(@RequestParam List<Long> genres) {
        return gameService.getGamesByGenre(genres);
    }
    
    @PostMapping("/sync")
    public ResponseEntity<String> syncGames(){
        gameService.syncGamesFromApi();
        return ResponseEntity.ok("Sincronização concluída!");
    }
    
    /*@GetMapping("/debug/raw")
    public ResponseEntity<String> debugRaw() {
    String raw = gameService.getAllGamesRawDebug();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(raw == null ? "" : raw);
}*/

}
