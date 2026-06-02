package com.weathermood.weathermood.contents;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class JikanClient {

    private final RestClient restClient = RestClient.create("https://api.jikan.moe/v4");

    public JikanAnimeResponse getTopAnime(Integer page) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/top/anime")
                        .queryParam("page", page)
                        .queryParam("limit", 12)
                        .build()
                )
                .retrieve()
                .body(JikanAnimeResponse.class);
    }
//인기 애니 조회 
    public JikanAnimeResponse searchAnime(String keyword, Integer page) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/anime")
                        .queryParam("q", keyword)
                        .queryParam("page", page)
                        .queryParam("limit", 12)
                        .build()
                )
                .retrieve()
                .body(JikanAnimeResponse.class);
    }
}
//애니 검색
//jikan api 호출 파일