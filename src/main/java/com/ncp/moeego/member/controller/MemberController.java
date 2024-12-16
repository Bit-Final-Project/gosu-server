package com.ncp.moeego.member.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.exception.GlobalExceptionHandler;
import com.ncp.moeego.member.bean.LoginDTO;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.member.service.impl.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.bean.JoinDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailServiceImpl mailService;
    private int number; // 이메일 인증 숫자를 저장하는 변수

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
    public ResponseEntity<ApiResponse> signOut(@RequestBody SignOutDTO signOutDTO) {
        try {
            // 비밀번호 확인
            boolean isMember = memberService.checkMember(signOutDTO.getEmail(), signOutDTO.getPwd());
            if (!isMember) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.error("비밀번호가 다릅니다", HttpStatus.BAD_REQUEST.name())
                );
            }

            // 회원 탈퇴 처리
            ApiResponse response = memberService.cancelMember(signOutDTO);
            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.resolve(Integer.parseInt(response.getErrorCode()));
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("서버에서 문제가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())
            );
        }
    }

    @PatchMapping("/mypage/account/private/update/name")
    public ResponseEntity<ApiResponse> updateName(@RequestBody Map<String, String> payload, Authentication authentication) {
        try {
            String name = payload.get("nickname");
            String email = authentication.getName(); // JWT에서 사용자 이메일 추출
            String updatedName = memberService.updateName(email, name);
            return ResponseEntity.ok(ApiResponse.success("회원 이름이 성공적으로 변경되었습니다.", Map.of("updateName", updatedName)));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.name())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("회원 이름 수정 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())
            );
        }
    }

    // 인증 이메일 전송
    @PostMapping("/mypage/account/private/mailSend")
    public ResponseEntity<ApiResponse> mailSend(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email").trim(); // 공백 제거
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("이메일이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.name()));
            }

            // 이메일 발송
            number = mailService.sendMail(email);
            return ResponseEntity.ok(ApiResponse.success("메일이 성공적으로 발송되었습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("메일 발송 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())
            );
        }
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mypage/account/private/mailCheck")
    public ResponseEntity<ApiResponse> mailCheck(@RequestBody Map<String, String> payload) {
        try {
            String num = payload.get("num").trim();
            boolean isMatch = num.equals(String.valueOf(number));

            if (isMatch) {
                return ResponseEntity.ok(ApiResponse.success("인증번호가 일치합니다.", null));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.name()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())
            );
        }
    }
    
}