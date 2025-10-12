package com.game_list.gamelist;

import com.game_list.gamelist.service.GameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GamelistApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamelistApplication.class, args);
	}
        
        /*@Bean
        CommandLineRunner run(GameService gameService){
            return args -> {
                gameService.syncGamesFromApi();
                
            };
        }*/
}
