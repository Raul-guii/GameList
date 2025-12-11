package com.game_list.gamelist.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_list.gamelist.dto.GameDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IgdbClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String CLIENT_ID = "8g0cj3b9mj3wbkv68tkz28qfffs24s";
    private final String ACCESS_TOKEN = "ddnpnxhggk65btfo4heu4nrg4029vt";
    private final String URL = "https://api.igdb.com/v4/games";

    public IgdbClient() {
        this.restTemplate = new RestTemplate();
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<GameDTO> sendRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", CLIENT_ID);
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            GameDTO[] games = mapper.readValue(response.getBody(), GameDTO[].class);

            for (GameDTO g : games) {
                if (g.getCover() != null && g.getCover().getUrl().startsWith("//")) {
                    g.getCover().setUrl("https:" + g.getCover().getUrl());
                }
            }

            return Arrays.asList(games);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public List<GameDTO> getGamesByGenres(List<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return List.of(); 
        }

        
        String idsString = genreIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String body = "fields id, name, rating, summary, cover.url, genres.name; "
                + "where genres = (" + idsString + ");";

        return sendRequest(body);
    }
    
    
}
