package com.weathermood.weathermood.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContentRecommendationResponse {

    private Long contentId;
    private String externalApiId;
    private String title;
    private String contentType;
    private String genre;
    private Integer matchScore;
    private String reason;
    private String posterUrl;

    public static ContentRecommendationResponse of(
            Content content,
            Integer matchScore,
            String reason
    ) {
        return new ContentRecommendationResponse(
                content.getContentId(),
                content.getExternalApiId(),
                content.getTitle(),
                content.getContentType(),
                content.getGenre(),
                matchScore,
                reason,
                content.getPosterUrl()
        );
    }

    public static ContentRecommendationResponse fromAnime(
            AnimeContentResponse anime,
            Integer matchScore,
            String reason
    ) {
        return new ContentRecommendationResponse(
                null,
                anime.getExternalApiId(),
                anime.getTitle(),
                anime.getContentType(),
                anime.getGenre(),
                matchScore,
                reason,
                anime.getPosterUrl()
        );
    }

    public static ContentRecommendationResponse fromDrama(
            DramaContentResponse drama,
            Integer matchScore,
            String reason
    ) {
        return new ContentRecommendationResponse(
                null,
                drama.getExternalApiId(),
                drama.getTitle(),
                drama.getContentType(),
                drama.getGenre(),
                matchScore,
                reason,
                drama.getPosterUrl()
        );
    }
}
//시뮬레이션 추천 결과 사용에 필요함