package com.weathermood.weathermood.simulation;

import com.weathermood.weathermood.contents.ContentRecommendationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecommendationBundleResponse {

    private List<ContentRecommendationResponse> prescription;
    private List<ContentRecommendationResponse> reverse;
    private RecommendationSetResponse set;
}