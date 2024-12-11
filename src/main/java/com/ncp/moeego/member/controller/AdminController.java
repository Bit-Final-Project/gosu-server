package com.ncp.moeego.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.bean.MemberSummaryDTO;

import com.ncp.moeego.member.service.AdminService;

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
    
    
    
    
    //고수 권한 페이지
    @GetMapping("/admin/pro/approval")
    public ResponseEntity<List<MemberSummaryDTO>> proApproval() {
        List<MemberSummaryDTO> pendingProMembers = adminService.getPendingProMembers();
        return ResponseEntity.ok(pendingProMembers);
    }
    
    
    // 고수 승인 버튼 클릭 시
    @PostMapping("/admin/pro/approve/{member_no}")
    public ResponseEntity<String> approveMember(@PathVariable("member_no") long member_no) {
        boolean result = adminService.approveMember(member_no);
        if (result) {
            return ResponseEntity.ok("고수 승인 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("승인 실패");
        }
    }
    
    // 고수 취소 버튼 클릭시
    @PostMapping("/admin/pro/cancel/{member_no}")
    public ResponseEntity<String> cancelMember(@PathVariable("member_no") long member_no) {
        boolean result = adminService.cancelMember(member_no);
        if (result) {
            return ResponseEntity.ok("고수 취소 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("취소 실패");
        }
    }
    
    
}
