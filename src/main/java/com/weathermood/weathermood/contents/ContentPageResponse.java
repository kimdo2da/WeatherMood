package com.weathermood.weathermood.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContentPageResponse<T> {

    private List<T> items;
    private PageInfo pageInfo;

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private Integer page;
        private Boolean hasNext;
    }
}