package com.ncp.moeego.pro.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.pro.dto.*;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.service.ProService;
import com.ncp.moeego.review.bean.ItemReviewResponse;
import com.ncp.moeego.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pro")
public class ProController {

    private final ProService proService;
    private final MemberService memberService;
    private final ReviewService reviewService;

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

    //달인 신청
    @PostMapping("/access")
    public ResponseEntity<?> proAccess(@RequestBody ProApplyRequest proApplyRequest, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("인증 정보가 없습니다. 다시 로그인하세요.", HttpStatus.UNAUTHORIZED.name())
            );
        }

        try {
            ApiResponse response = proService.proAccess(authentication.getName(), proApplyRequest);
            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.resolve(Integer.parseInt(response.getErrorCode()));
            return ResponseEntity.status(status).body(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.name())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.error("이미 달인 권한이 있습니다.", HttpStatus.BAD_REQUEST.name())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("달인 신청 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())
            );
        }

    }

    //달인 소개
    @GetMapping("/intro")
    public ResponseEntity<?> intro(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("인증 정보가 없습니다. 다시 로그인하세요.", HttpStatus.UNAUTHORIZED.name())
            );
        }

        try {
            Pro pro = proService.getProByMemberNo(memberService.getMemberNo(authentication.getName()));
            Map<String, String> map = new HashMap<>();
            map.put("intro", pro.getIntro());
            map.put("oneIntro", pro.getOneIntro());
            return ResponseEntity.ok(ApiResponse.success("소개를 불러오는데 성공하였습니다.", map));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("소개를 불러오는 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())
            );
        }
    }

    //달인 소개 수정
    @PutMapping("/intro")
    public ResponseEntity<?> introUpdate(@RequestBody Map<String, String> payload, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("인증 정보가 없습니다. 다시 로그인하세요.", HttpStatus.UNAUTHORIZED.name())
            );
        }

        try {
            ApiResponse response = proService.updateIntro(authentication.getName(), payload);
            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.resolve(Integer.parseInt(response.getErrorCode()));
            return ResponseEntity.status(status).body(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.name())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.error("달인 권한이 없습니다.", HttpStatus.BAD_REQUEST.name())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("수정 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())
            );
        }
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
    public ResponseEntity<?> getItemList(@RequestParam(value = "subCateNo", required = false) Long subCateNo, @RequestParam(value = "location", required = false) String location, @RequestParam(value = "value", required = false) String value, @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {
        log.info(location + subCateNo + value);
        return ResponseEntity.ok(ApiResponse.success("조회 성공", proService.getItemList(subCateNo, location, value, pg)));

    }

    // 메인 화면 지도
    @GetMapping("/item/main")
    public ResponseEntity<?> getMainItem(@RequestParam(value = "subCateNo", required = false) Long subCateNo, @RequestParam(value = "location", required = false) String location, @RequestParam(value = "value", required = false) String value, @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {
        log.info(location + subCateNo + value);
        return ResponseEntity.ok(ApiResponse.success("조회 성공", proService.getMainItem(subCateNo, location, value, pg)));

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