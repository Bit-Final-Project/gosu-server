package com.ncp.moeego.member.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ncp.moeego.member.bean.CancelDTO;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.ProDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.service.AdminService;
import com.ncp.moeego.pro.entity.Pro;

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
    @GetMapping("/admin/member")
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
    
    // 탈퇴 회원(ROLE_CANCEL) 수 ( 관리자 페이지 차트 데이터화 활용 )
    @GetMapping("/admin/leave")
    public ResponseEntity<Integer> leave(){
    	int count = adminService.getRoleCancelCount();
    	return ResponseEntity.ok(count);
    }
    
    
    
    //회원가입 일주일 치 데이터화 ( 관리자 페이지 차트 데이터화 활용 )
    @GetMapping("/admin/weekmember")
    public ResponseEntity<List<Map<String , Object>>> getWeekMemberData(){
    	List<Map<String , Object>> weekmemberData = adminService.getWeekMemberData();
    	return ResponseEntity.ok(weekmemberData);
    }
    
    //고수 등록 일주일 치 데이터화 ( 관리자 페이지 차트 데이터화 활용 )
    @GetMapping("/admin/weekpro")
    public ResponseEntity<List<Map<String, Object>>> getProMemberJoinData() {
    	LocalDateTime startDateTime = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);  // 일주일 전 자정
        LocalDateTime endDateTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);  // 오늘 자정

        List<Map<String, Object>> result = adminService.getProMemberJoinData(startDateTime, endDateTime);
        return ResponseEntity.ok(result);
    }
    
    // 탈퇴 회원 일주일 치 데이터화 ( 관리자 페이지 차트 데이터화 활용 )
    @GetMapping("/admin/weekleave")
    public ResponseEntity<List<Map<String, Object>>> getCancelledMembersData() {
        // 예시로 현재 날짜와 일주일 전 날짜를 사용하여 데이터를 조회한다고 가정
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);  // 일주일 전 자정
        LocalDateTime endDateTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);  // 오늘 자정

        List<Map<String, Object>> result = adminService.getCancelledMemberData(startDateTime, endDateTime);
        return ResponseEntity.ok(result);
    }
    
    
    //고수 권한 페이지
    @GetMapping("/admin/pro/approval")
    public ResponseEntity<List<MemberSummaryDTO>> proApproval() {
        List<MemberSummaryDTO> pendingProMembers = adminService.getPendingProMembers(MemberStatus.ROLE_PEND_PRO);
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
    
    
    // 고수 박탈 버튼 클릭시
    @PostMapping("/admin/member/pro/cancel/{memberNo}")
    public ResponseEntity<String> revokePro(@PathVariable("memberNo") Long memberNo) {
        try {
            adminService.revokeMember(memberNo);  // 서비스에서 박탈 처리
            return ResponseEntity.ok("박탈 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("박탈 처리 오류");
        }
    }
    
    
    // 일반 회원 조회
    @GetMapping("/admin/member/user")
    public ResponseEntity<List<Member>> getUserMembers() {
        List<Member> userData = adminService.getUserMembers();
        return ResponseEntity.ok(userData);
    }

    
    // 고수 회원 조회
    @GetMapping("/admin/member/pro")
    public ResponseEntity<List<ProDTO>> getProMembersWithDetails() {
        List<ProDTO> proMembers = adminService.getProMembersWithDetails();
        return ResponseEntity.ok(proMembers);
    }
    
    // 탈퇴 회원 조회
    @GetMapping("/admin/member/cancel")
    public ResponseEntity<List<CancelDTO>> getCancelMembersWithDetails() {
        List<CancelDTO> cancelMembers = adminService.getCancelMembersWithDetails();
        return ResponseEntity.ok(cancelMembers);
    }

    
    
    
}
