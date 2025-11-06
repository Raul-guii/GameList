
package com.game_list.gamelist.dto;

import com.game_list.gamelist.entity.Genre;
import java.util.List;

public class GameDTO {
   
    private Long id;
    private String name;
    private Double rating;
    private Cover cover;
    private String summary;
    private List<Genre> genres;
    
    private String coverUrl;
    private List<String> genreNames;
    
    public GameDTO(){}

    public GameDTO(Long id, String name, Double rating, Cover cover, String summary, List<Genre> genres, String coverUrl, List<String> genreNames) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.cover = cover;
        this.summary = summary;
        this.genres = genres;
        this.coverUrl = coverUrl;
        this.genreNames = genreNames;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<String> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(List<String> genreNames) {
        this.genreNames = genreNames;
    }
            
    public static class Cover{
        private String url;
        
        public String getUrl(){
            return url; 
        }
        public void setUrl(String url){
            this.url = url;
        }
    }
    
   
}
