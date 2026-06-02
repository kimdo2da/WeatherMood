package com.weathermood.weathermood.contents;

import lombok.Getter;

import java.util.List;

@Getter
public class JikanAnimeResponse {

    private List<AnimeItem> data;
    private Pagination pagination;

    @Getter
    public static class AnimeItem {
        private Long mal_id;
        private String title;
        private String synopsis;
        private Double score;
        private Images images;
        private List<Genre> genres;
    }

    @Getter
    public static class Images {
        private ImageSet jpg;
        private ImageSet webp;
    }

    @Getter
    public static class ImageSet {
        private String image_url;
        private String small_image_url;
        private String large_image_url;
    }

    @Getter
    public static class Genre {
        private Long mal_id;
        private String name;
    }

    @Getter
    public static class Pagination {
        private Boolean has_next_page;
    }
}
//jikan api 응답 dto
// data 배열 안에 애니 목록이 들어와있음 jikan
//페이지 정보는 pagination.has_next_page 형태