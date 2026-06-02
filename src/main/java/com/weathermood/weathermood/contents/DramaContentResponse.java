package com.weathermood.weathermood.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DramaContentResponse {

    private String externalApiId;
    private String title;
    private String contentType;
    private String genre;
    private String description;
    private String posterUrl;
    private Double score;
}
//드라마 응답용 dto