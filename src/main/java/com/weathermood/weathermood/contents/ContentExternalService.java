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
    private final TmdbClient tmdbClient;

    public ContentPageResponse<AnimeContentResponse> getAnimeList(Integer page) {
        int safePage = normalizePage(page);

        JikanAnimeResponse response = jikanClient.getTopAnime(safePage);

        return toAnimePageResponse(response, safePage);
    }
//애니 목록 조회 jikan에서 애니 목록응답받기 
    public ContentPageResponse<AnimeContentResponse> searchAnime(String keyword, Integer page) {
        int safePage = normalizePage(page);

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }

        JikanAnimeResponse response = jikanClient.searchAnime(keyword, safePage);

        return toAnimePageResponse(response, safePage);
    }
//검색
    public ContentPageResponse<DramaContentResponse> getDramaList(Integer page) {
        int safePage = normalizePage(page);

        TmdbDramaResponse response = tmdbClient.getPopularDramas(safePage);

        return toDramaPageResponse(response, safePage);
    }
//드라마 tmdb 
    public ContentPageResponse<DramaContentResponse> searchDramas(String keyword, Integer page) {
        int safePage = normalizePage(page);

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }

        TmdbDramaResponse response = tmdbClient.searchDramas(keyword, safePage);

        return toDramaPageResponse(response, safePage);
    }
    // 드라마 검색
    public List<PopularContentResponse> getPopularContents() {
        List<AnimeContentResponse> animeItems = getAnimeList(1).getItems();
        List<DramaContentResponse> dramaItems = getDramaList(1).getItems();

        List<PopularContentResponse> popularItems = new java.util.ArrayList<>();

        animeItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .limit(3)
                .map(PopularContentResponse::fromAnime)
                .forEach(popularItems::add);

        dramaItems.stream()
                .filter(item -> item.getPosterUrl() != null)
                .limit(2)
                .map(PopularContentResponse::fromDrama)
                .forEach(popularItems::add);

        java.util.Collections.shuffle(popularItems);

        return popularItems.stream()
                .limit(5)
                .toList();
    }
//인기 콘텐츠 조회로직
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
    //애니 변환 data> animecontentresponse로 변환

    private AnimeContentResponse toAnimeContentResponse(JikanAnimeResponse.AnimeItem item) {
        return new AnimeContentResponse(
                item.getMal_id() == null ? null : String.valueOf(item.getMal_id()),
                item.getTitle(),
                "ANIME",
                toAnimeGenreText(item.getGenres()),
                item.getSynopsis(),
                getAnimePosterUrl(item),
                item.getScore()
        );
    }
    //jikan api 필드를 우리 프론트 필드로 변환

    private String toAnimeGenreText(List<JikanAnimeResponse.Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return "";
        }

        return genres.stream()
                .map(JikanAnimeResponse.Genre::getName)
                .collect(Collectors.joining(", "));
    }
//장르를 변환
    private String getAnimePosterUrl(JikanAnimeResponse.AnimeItem item) {
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
//포스터 url 추출 이미지 
    private ContentPageResponse<DramaContentResponse> toDramaPageResponse(
            TmdbDramaResponse response,
            Integer page
    ) {
        List<DramaContentResponse> items = response == null || response.getResults() == null
                ? Collections.emptyList()
                : response.getResults()
                .stream()
                .map(this::toDramaContentResponse)
                .collect(Collectors.toList());

        boolean hasNext = response != null
                && response.getTotal_pages() != null
                && page < response.getTotal_pages();

        return new ContentPageResponse<>(
                items,
                new ContentPageResponse.PageInfo(page, hasNext)
        );
    }
//드라마 results> dramacontentresponse로 변환
    private DramaContentResponse toDramaContentResponse(TmdbDramaResponse.DramaItem item) {
        return new DramaContentResponse(
                item.getId() == null ? null : String.valueOf(item.getId()),
                item.getName() != null ? item.getName() : item.getOriginal_name(),
                "DRAMA",
                toDramaGenreText(item.getGenre_ids()),
                item.getOverview(),
                getDramaPosterUrl(item.getPoster_path()),
                item.getVote_average()
        );
    }
    //드라마 변환 tmdb 드라마 데이터를 프론트 응답용 dto로 

    private String toDramaGenreText(List<Integer> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return "";
        }

        return genreIds.stream()
                .map(this::convertTmdbGenre)
                .collect(Collectors.joining(", "));
    }
//장르 변환
    private String convertTmdbGenre(Integer genreId) {
        if (genreId == null) {
            return "";
        }

        return switch (genreId) {
            case 10759 -> "Action & Adventure";
            case 16 -> "Animation";
            case 35 -> "Comedy";
            case 80 -> "Crime";
            case 99 -> "Documentary";
            case 18 -> "Drama";
            case 10751 -> "Family";
            case 10762 -> "Kids";
            case 9648 -> "Mystery";
            case 10763 -> "News";
            case 10764 -> "Reality";
            case 10765 -> "Sci-Fi & Fantasy";
            case 10766 -> "Soap";
            case 10767 -> "Talk";
            case 10768 -> "War & Politics";
            case 37 -> "Western";
            default -> "Unknown";
        };
    }

    private String getDramaPosterUrl(String posterPath) {
        if (posterPath == null || posterPath.isBlank()) {
            return null;
        }

        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
//tmdb는 포스터를 path만 제공> 주소를붙여서 실제 이미지 url로 사용
    private int normalizePage(Integer page) {
        if (page == null || page < 1) {
            return 1;
        }

        return page;
    }
}