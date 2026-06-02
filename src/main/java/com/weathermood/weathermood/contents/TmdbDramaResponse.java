package com.weathermood.weathermood.contents;

import lombok.Getter;

import java.util.List;

@Getter
public class TmdbDramaResponse {

    private Integer page;
    private List<DramaItem> results;
    private Integer total_pages;
    private Integer total_results;

    @Getter
    public static class DramaItem {
        private Long id;
        private String name;
        private String original_name;
        private String overview;
        private String poster_path;
        private Double vote_average;
        private List<Integer> genre_ids;
        private String first_air_date;
    }
}
//tmdb 응답 dto 드라마 목록은 results에 들어옴
//tmdb 는 포스터를 poster_path만 주고 실제 이미지 url은
//앞에 https://image.tmdb.org/t/p/w500을 써야함