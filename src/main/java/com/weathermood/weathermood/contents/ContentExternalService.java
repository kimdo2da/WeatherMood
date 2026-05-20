package com.weathermood.weathermood.contents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentExternalService {

    private final JikanClient jikanClient;

    public ContentPageResponse<AnimeContentResponse> getAnimeList(Integer page) {
        int safePage = normalizePage(page);

        JikanAnimeResponse response = jikanClient.getTopAnime(safePage);

        return toAnimePageResponse(response, safePage);
    }

    public ContentPageResponse<AnimeContentResponse> searchAnime(String keyword, Integer page) {
        int safePage = normalizePage(page);

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }

        JikanAnimeResponse response = jikanClient.searchAnime(keyword, safePage);

        return toAnimePageResponse(response, safePage);
    }

    private ContentPageResponse<AnimeContentResponse> toAnimePageResponse(
            JikanAnimeResponse response,
            Integer page
    ) {
        List<AnimeContentResponse> items = response == null || response.getData() == null
                ? Collections.emptyList()
                : response.getData()
                .stream()
                .map(this::toAnimeContentResponse)
                .collect(Collectors.toList());

        Boolean hasNext = response != null
                && response.getPagination() != null
                && Boolean.TRUE.equals(response.getPagination().getHas_next_page());

        return new ContentPageResponse<>(
                items,
                new ContentPageResponse.PageInfo(page, hasNext)
        );
    }

    private AnimeContentResponse toAnimeContentResponse(JikanAnimeResponse.AnimeItem item) {
        return new AnimeContentResponse(
                item.getMal_id() == null ? null : String.valueOf(item.getMal_id()),
                item.getTitle(),
                "ANIME",
                toGenreText(item.getGenres()),
                item.getSynopsis(),
                getPosterUrl(item),
                item.getScore()
        );
    }

    private String toGenreText(List<JikanAnimeResponse.Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return "";
        }

        return genres.stream()
                .map(JikanAnimeResponse.Genre::getName)
                .collect(Collectors.joining(", "));
    }

    private String getPosterUrl(JikanAnimeResponse.AnimeItem item) {
        if (item.getImages() == null) {
            return null;
        }

        if (item.getImages().getWebp() != null
                && item.getImages().getWebp().getLarge_image_url() != null) {
            return item.getImages().getWebp().getLarge_image_url();
        }

        if (item.getImages().getJpg() != null) {
            return item.getImages().getJpg().getImage_url();
        }

        return null;
    }

    private int normalizePage(Integer page) {
        if (page == null || page < 1) {
            return 1;
        }

        return page;
    }
}