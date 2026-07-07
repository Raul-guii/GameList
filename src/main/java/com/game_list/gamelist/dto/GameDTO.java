package com.game_list.gamelist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GameDTO {

    private Long id;
    private String name;
    private Double rating;
    private String summary;
    private Cover cover;
    private List<GenreRef> genres;

    /**
     * Helper de conveniência: extrai a URL da capa do objeto aninhado.
     * Evita repetir "dto.getCover() != null ? dto.getCover().getUrl() : null"
     * em vários services.
     */
    public String getCoverUrl() {
        return cover != null ? cover.getUrl() : null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Cover {
        private String url;
    }

    /**
     * Representa o gênero retornado pela IGDB (apenas id e name),
     * sem depender da entity JPA Genre — evita misturar DTO de API externa
     * com entidade de persistência.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class GenreRef {
        private Long id;
        private String name;
    }
}