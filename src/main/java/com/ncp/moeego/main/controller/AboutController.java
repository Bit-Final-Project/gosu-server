package com.ncp.moeego.main.controller;

import com.ncp.moeego.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AboutController {
    private final MemberService memberService;

    // 카테고리별 달인 인원 수 체크
    @GetMapping("/about")
    public ResponseEntity<?> getProCountList() {
        return ResponseEntity.ok(memberService.getProCountList());
    }
}
