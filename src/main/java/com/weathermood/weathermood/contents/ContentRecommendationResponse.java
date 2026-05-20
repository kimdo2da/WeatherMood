package com.weathermood.weathermood.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContentRecommendationResponse {

    private Long contentId;
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
                content.getTitle(),
                content.getContentType(),
                content.getGenre(),
                matchScore,
                reason,
                content.getPosterUrl()
        );
    }
}