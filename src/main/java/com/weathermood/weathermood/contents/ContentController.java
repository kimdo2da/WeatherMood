package com.weathermood.weathermood.contents;

import com.weathermood.weathermood.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentExternalService contentExternalService;

    @GetMapping("/anime")
    public ApiResponse<ContentPageResponse<AnimeContentResponse>> getAnimeList(
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        ContentPageResponse<AnimeContentResponse> response =
                contentExternalService.getAnimeList(page);

        return ApiResponse.success(response, "애니 목록 조회에 성공했습니다.");
    }

    @GetMapping("/anime/search")
    public ApiResponse<ContentPageResponse<AnimeContentResponse>> searchAnime(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        ContentPageResponse<AnimeContentResponse> response =
                contentExternalService.searchAnime(keyword, page);

        return ApiResponse.success(response, "애니 검색에 성공했습니다.");
    }
}