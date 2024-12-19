package com.ncp.moeego.main.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.main.controller.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    // 검색 서비스 리스트
    @GetMapping("/item")
    public ResponseEntity<?> getSearchList(@RequestParam(value = "value", required = false) String value, @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {
        log.info(value + pg);
        return ResponseEntity.ok(ApiResponse.success("조회 성공", searchService.getSearchList(value, pg)));
    }
}
