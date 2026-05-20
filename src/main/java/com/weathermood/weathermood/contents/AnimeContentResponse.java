package com.weathermood.weathermood.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnimeContentResponse {

    private String externalApiId;
    private String title;
    private String contentType;
    private String genre;
    private String description;
    private String posterUrl;
    private Double score;
}