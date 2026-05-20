package com.weathermood.weathermood.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularContentResponse {

    private String externalApiId;
    private String title;
    private String contentType;
    private String genre;
    private String description;
    private String posterUrl;
    private Double score;

    public static PopularContentResponse fromAnime(AnimeContentResponse anime) {
        return new PopularContentResponse(
                anime.getExternalApiId(),
                anime.getTitle(),
                anime.getContentType(),
                anime.getGenre(),
                anime.getDescription(),
                anime.getPosterUrl(),
                anime.getScore()
        );
    }

    public static PopularContentResponse fromDrama(DramaContentResponse drama) {
        return new PopularContentResponse(
                drama.getExternalApiId(),
                drama.getTitle(),
                drama.getContentType(),
                drama.getGenre(),
                drama.getDescription(),
                drama.getPosterUrl(),
                drama.getScore()
        );
    }
}