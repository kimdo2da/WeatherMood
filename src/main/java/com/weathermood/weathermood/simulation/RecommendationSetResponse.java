package com.weathermood.weathermood.simulation;

import com.weathermood.weathermood.contents.ContentRecommendationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationSetResponse {

    private ContentRecommendationResponse anime;
    private ContentRecommendationResponse drama;
}