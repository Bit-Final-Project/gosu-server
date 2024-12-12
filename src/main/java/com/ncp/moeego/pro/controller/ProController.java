package com.ncp.moeego.pro.controller;

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
import java.util.List;
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
    public ResponseEntity<String> proJoin(@RequestBody ProJoinRequest proJoinRequest) {
        String response = proService.proJoin(proJoinRequest);

        return switch (response) {
            case "pro join success" -> ResponseEntity.status(HttpStatus.OK).body(response);
            case "join fail", "pro apply fail" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server error");
        };
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

        Map<String, Object> response = new HashMap<>();
        response.put("content", favoritePage.getContent());
        response.put("totalPages", favoritePage.getTotalPages());
        response.put("currentPage", favoritePage.getNumber());
        response.put("totalElements", favoritePage.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

        @DeleteMapping("/favorite")
        public ResponseEntity<?> deleteFavorites(@RequestBody FavoriteDeleteRequest favoriteDeleteRequest) {
            log.debug(favoriteDeleteRequest.toString());
            String response = proService.deleteFavorites(favoriteDeleteRequest.getMemberNo(), favoriteDeleteRequest.getProNo());

            if (response.equals("fail")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request");
            }
            return ResponseEntity.ok("delete success");
        }


}