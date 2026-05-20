package com.weathermood.weathermood.contents;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TmdbClient {

    private final RestClient restClient = RestClient.create("https://api.themoviedb.org/3");

    @Value("${tmdb.api.key}")
    private String apiKey;

    public TmdbDramaResponse getPopularDramas(Integer page) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/popular")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "ko-KR")
                        .queryParam("page", page)
                        .build()
                )
                .retrieve()
                .body(TmdbDramaResponse.class);
    }

    public TmdbDramaResponse searchDramas(String keyword, Integer page) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/tv")
                        .queryParam("api_key", apiKey)
                        .queryParam("query", keyword)
                        .queryParam("language", "ko-KR")
                        .queryParam("page", page)
                        .queryParam("include_adult", false)
                        .build()
                )
                .retrieve()
                .body(TmdbDramaResponse.class);
    }
}
// /tv/popular 은 인기 드라마 목룍 /search/tv 는 드라마 검색용
//https://api.themoviedb.org/3/search/tv이런식으로 사용 검색문서
//한국어로 검색하게될시 params keyword page 값 필수 