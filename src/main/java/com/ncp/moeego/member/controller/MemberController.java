package com.ncp.moeego.member.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.exception.GlobalExceptionHandler;
import com.ncp.moeego.member.bean.LoginDTO;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.bean.JoinDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity joinProcess(@RequestBody JoinDTO joinDTO) {
        boolean check = memberService.write(joinDTO);
        if(check) return ResponseEntity.ok("ok");
        else return  ResponseEntity.badRequest().body("값이 잘못 되었습니다");
    }

    @PostMapping("/join/exist")
    public boolean isExistEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        return memberService.isExist(email);
    }

    @PatchMapping("/mypage/account/private/signout")
    public ResponseEntity checkLogin(@RequestBody SignOutDTO signOutDTO) {
        boolean check = memberService.checkMember(signOutDTO.getEmail(), signOutDTO.getPwd());
        if(check) return ResponseEntity.ok(ApiResponse.success("T", memberService.cancelMember(signOutDTO)));
        else throw new IllegalArgumentException();
    }

    @PatchMapping("/mypage/account/private/update/name")
    public void updateName(@RequestBody String name) {
        System.out.println(name);
    }
    
}