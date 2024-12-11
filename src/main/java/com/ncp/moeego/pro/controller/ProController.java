package com.ncp.moeego.pro.controller;

import com.ncp.moeego.member.service.impl.MemberServiceImpl;
import com.ncp.moeego.pro.dto.ProJoinRequest;
import com.ncp.moeego.pro.service.ProServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public boolean isExistingEmail(@RequestBody String email) {
        return memberService.isExist(email);

    }


}
