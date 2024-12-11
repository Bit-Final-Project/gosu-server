package com.ncp.moeego.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ncp.moeego.member.service.AdminService;
import com.ncp.moeego.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
	
	private final AdminService adminService;
	
    @PostMapping("/admin")
    public ResponseEntity adminP() {
        return ResponseEntity.ok("admin");
    }
    
    
    // 전체 회원(ROLE_USER) 수 ( 관리자 페이지 차트 데이터화 활용 )
    @GetMapping("/admin/all")
    public ResponseEntity<Integer> all() {
    	int count = adminService.getRoleUserCount();
    	return ResponseEntity.ok(count);
    }
    
    // 프로 회원(ROLE_PRO) 수 ( 관리자 페이지 차트 데이터화 활용 )
    @GetMapping("/admin/pro")
    public ResponseEntity<Integer> pro(){
    	int count = adminService.getRoleProCount();
    	return ResponseEntity.ok(count);
    }
    
    // 전체 탈퇴 회원(ROLE_CANCEL) 수 ( 관리자 페이지 차트 데이터화 활용 )
    @GetMapping("/admin/leave")
    public ResponseEntity<Integer> leave(){
    	int count = adminService.getRoleCancelCount();
    	return ResponseEntity.ok(count);
    }
    
    
    
    //회원가입, 고수 , 탈퇴 일주일 치 데이터화 ( 관리자 페이지 차트 데이터화 활용 )
//    @GetMapping("/admin/member-status")
//    public ResponseEntity<Map<String, List<MemberStatsDTO>>> getMemberStats(){
//    	
//    }
}
