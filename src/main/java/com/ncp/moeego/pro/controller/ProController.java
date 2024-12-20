package com.ncp.moeego.pro.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.pro.dto.*;
import com.ncp.moeego.pro.service.ProService;
import com.ncp.moeego.review.bean.ItemReviewResponse;
import com.ncp.moeego.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pro")
public class ProController {

    private final ProService proService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    public ProController(ProService proService, MemberService memberService, ReviewService reviewService) {
        this.proService = proService;
        this.memberService = memberService;
        this.reviewService = reviewService;
    }


    @PostMapping("/join")
    public ResponseEntity<?> proJoin(@RequestBody ProJoinRequest proJoinRequest) {
        String message = proService.proJoin(proJoinRequest);
        return ResponseEntity.ok(ApiResponse.success(message));
    }


    @PostMapping("/join/exist")
    public boolean isExistingEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        return memberService.isExist(email);
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorites(@RequestParam("memberNo") Long memberNo, @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {

        Page<FavoriteResponse> favoritePage = proService.getFavorites(memberNo, pg);

        if (favoritePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("content", favoritePage.getContent());
        data.put("totalPages", favoritePage.getTotalPages());
        data.put("currentPage", favoritePage.getNumber());
        data.put("totalElements", favoritePage.getTotalElements());

        return ResponseEntity.ok(ApiResponse.success("조회성공", data));
    }

    @PostMapping("/favorite")
    public ResponseEntity<?> postFavorites(@RequestBody FavoritePostRequest favoritePostRequest) {
        log.info("postFavorites 요청: memberNo:{}, proNo:{}", favoritePostRequest.getMemberNo(), favoritePostRequest.getProNo());
        return ResponseEntity.ok(ApiResponse.success(proService.postFavorites(favoritePostRequest)));

    }

    @DeleteMapping("/favorite")
    public ResponseEntity<?> deleteFavorites(@RequestBody FavoriteDeleteRequest favoriteDeleteRequest) {
        log.info("deleteFavorites 요청: memberNo={}, proNoList={}", favoriteDeleteRequest.getMemberNo(), favoriteDeleteRequest.getProNo());
        String message = proService.deleteFavorites(favoriteDeleteRequest.getMemberNo(), favoriteDeleteRequest.getProNo());

        return ResponseEntity.ok(ApiResponse.success(message));
    }

    //서비스 등록
    @PutMapping("/item")
    public ResponseEntity<?> postItem(@RequestBody PostItemRequest postItemRequest) {
        log.info("postItem 요청: " + postItemRequest.toString());

        String message = proService.postItem(postItemRequest);

        return ResponseEntity.ok(ApiResponse.success(message));

    }

    // 서비스 등록 폼 진입 시 초기값 반환
    @GetMapping("/item/init")
    public ResponseEntity<?> getItemInit(@RequestParam("memberNo") Long memberNo) {
        log.info("달인 서비스 등록 요청: " + memberNo);

        return ResponseEntity.ok(ApiResponse.success("조회 성공", proService.getInitItem(memberNo)));

    }

    // 달인 서비스 리스트
    @GetMapping("/item")
    public ResponseEntity<?> getItemList(@RequestParam(value = "subCateNo", required = false) Long subCateNo, @RequestParam(value = "location", required = false) String location, @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {
        log.info(location + subCateNo);
        return ResponseEntity.ok(ApiResponse.success("조회 성공", proService.getItemList(subCateNo, location, pg)));

    }

    // 달인 서비스 상세보기
    // 위에 리스트 반환시 상세정보 다 넣어서 반환했기때문에 해당 서비스의 리뷰만 리턴
    @GetMapping("/item/detail")
    public ResponseEntity<?> getItemDetails(@RequestParam("proItemNo") Long proItemNo, @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {
        Page<ItemReviewResponse> reviewResponsePage = reviewService.getReviewsByItemNo(proItemNo, pg);
        Map<String, Object> response = new HashMap<>();
        response.put("content", reviewResponsePage.getContent());
        response.put("totalPages", reviewResponsePage.getTotalPages());
        response.put("currentPage", reviewResponsePage.getNumber());
        response.put("totalElements", reviewResponsePage.getTotalElements());
        return ResponseEntity.ok(ApiResponse.success("조회 성공", response));

    }

}