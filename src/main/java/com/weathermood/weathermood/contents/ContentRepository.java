package com.weathermood.weathermood.contents;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByRouteId(Long routeId);

    List<Content> findByRouteIdAndContentType(Long routeId, String contentType);
}
// findbyrouteid 처방전 추천용
// findbyrouteidandcontenttype 애니 1개 드라마 1개 뽑기용