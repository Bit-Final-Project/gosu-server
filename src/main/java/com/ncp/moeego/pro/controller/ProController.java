package com.ncp.moeego.pro.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.service.impl.MemberServiceImpl;
import com.ncp.moeego.pro.dto.FavoriteDeleteRequest;
import com.ncp.moeego.pro.dto.FavoriteResponse;
import com.ncp.moeego.pro.dto.ProJoinRequest;
import com.ncp.moeego.pro.service.ProServiceImpl;
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

    private final ProServiceImpl proService;
    private final MemberServiceImpl memberService;

    public ProController(ProServiceImpl proService, MemberServiceImpl memberService) {
        this.proService = proService;
        this.memberService = memberService;
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

        return ResponseEntity.ok(ApiResponse.success("success", data));
    }

    @DeleteMapping("/favorite")
    public ResponseEntity<?> deleteFavorites(@RequestBody FavoriteDeleteRequest favoriteDeleteRequest) {
        log.info("deleteFavorites 요청: memberNo={}, proNoList={}", favoriteDeleteRequest.getMemberNo(), favoriteDeleteRequest.getProNo());
        String message = proService.deleteFavorites(favoriteDeleteRequest.getMemberNo(), favoriteDeleteRequest.getProNo());

        return ResponseEntity.ok(ApiResponse.success(message));
    }


}