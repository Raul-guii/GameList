package com.game_list.gamelist.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_list.gamelist.dto.GameDTO;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class IgdbClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${TWITCH_CLIENT_ID}")
    private String clientId;

    @Value("${TWITCH_CLIENT_SECRET}")
    private String clientSecret;

    private static final String TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    private static final String GAMES_URL = "https://api.igdb.com/v4/games";

    private String accessToken;
    private Instant tokenExpiresAt = Instant.EPOCH;

    public IgdbClient() {
        this.restTemplate = new RestTemplate();
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private synchronized String getValidAccessToken() {
        if (accessToken != null && Instant.now().isBefore(tokenExpiresAt)) {
            return accessToken;
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "client_credentials");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<TwitchTokenResponse> response = restTemplate.postForEntity(
                TOKEN_URL, request, TwitchTokenResponse.class
        );

        TwitchTokenResponse body = response.getBody();
        if (body == null || body.getAccessToken() == null) {
            throw new IllegalStateException("Falha ao obter access token da Twitch");
        }

        this.accessToken = body.getAccessToken();
        this.tokenExpiresAt = Instant.now().plusSeconds(body.getExpiresIn() - 60);

        return accessToken;
    }

    public List<GameDTO> sendRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + getValidAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    GAMES_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            GameDTO[] games = mapper.readValue(response.getBody(), GameDTO[].class);

            for (GameDTO g : games) {
                if (g.getCover() != null && g.getCover().getUrl() != null
                        && g.getCover().getUrl().startsWith("//")) {
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

    /**
     * DTO interno apenas para desserializar a resposta de token da Twitch.
     * Não precisa de getters/setters completos do domínio do app.
     */
    private static class TwitchTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private long expiresIn;

        public String getAccessToken() {
            return accessToken;
        }

        public long getExpiresIn() {
            return expiresIn;
        }
    }
}