package com.ncp.moeego.member.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.article.bean.ArticleDTO;
import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.article.service.ArticleService;
import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.bean.ArticleImageDTO;
import com.ncp.moeego.member.bean.CancelDTO;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.ProDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.service.AdminService;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.member.service.impl.MailServiceImpl;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.reservation.controller.ReservationController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
	
	private final AdminService adminService;
	private final MemberService memberService;
	private final MailServiceImpl mailService;
	private final ArticleService articleService;
	
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
    public ResponseEntity<List<MemberSummaryDTO>> proApproval(
    		@RequestParam(value = "page", defaultValue = "1") int page, 
            @RequestParam(value = "size", defaultValue = "10") int size) {
    	
    	Pageable pageable = PageRequest.of(page - 1, size);
        List<MemberSummaryDTO> pendingProMembers = adminService.getPendingProMembers(pageable,MemberStatus.ROLE_PEND_PRO);
        return ResponseEntity.ok(pendingProMembers);
    }
    
    // 고수 신청 시 이메일 상태 값 확인
    @GetMapping("/admin/emailstatus")
    public ResponseEntity<?> getEmailStatus(Authentication authentication) {
        // 로그인한 사용자의 이름 가져오기
        String username = authentication.getName();
        log.info("Authenticated user: " + username);

        // 사용자 이름으로 emailStatus 가져오기
        Integer emailStatus = memberService.getEmailStatusByName(username);
        
        
        //emailStatus가 없으면 오류 반환
        if (emailStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없거나 이메일 상태를 찾을 수 없습니다.");
        }

        // emailStatus 반환
        return ResponseEntity.ok(emailStatus);

    }

    // 프론트에서 알림 아이콘 클릭 후 확인 할 시
    @PatchMapping("/admin/emailstatus")
    public ResponseEntity<?> updateEmailStatus(Authentication authentication) {
        // 현재 상태 조회
    	String username = authentication.getName();
        int currentStatus = memberService.getEmailStatusByName(username);
        
        if (currentStatus == 0) {
            return ResponseEntity.badRequest().body("이미 email_status가 0입니다.");
        }

        // 상태를 0으로 변경
        boolean updated = memberService.updateEmailStatus(username, 0); // 새 메서드 작성 필요
        if (updated) {
            return ResponseEntity.ok("email_status가 0으로 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("email_status 변경 실패");
        }
    }
    
    // 고수 승인 버튼 클릭 시
    @PostMapping("/admin/pro/approve/{member_no}")
    public ResponseEntity<ApiResponse> approveMember(@PathVariable("member_no") long member_no) {
        // 이메일 검사
        String email = memberService.getMemberEmail(member_no);
        boolean check = false;
        
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("이메일이 유효하지 않습니다.", "BAD_REQUEST"));
        }
        
        // 카카오/구글/네이버 회원 가입 한 사람들
        if (email.contains(" ")) {
            // 이메일이 유효하지 않으면 상태값만 변경
        	check = true;
            boolean result = adminService.approveMember(member_no, check);
            if (result) {
                return ResponseEntity.ok(ApiResponse.success("달인 승인 완료 (이메일 전송 생략)", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body(ApiResponse.error("달인 승인 실패", "BAD_REQUEST"));
            }
        }

        // 이메일 전송
        try {
            mailService.accessMail(email); // 이메일 전송
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(ApiResponse.error("이메일 전송 실패", "INTERNAL_SERVER_ERROR"));
        }

        // member_status 상태 변경
        boolean result = adminService.approveMember(member_no, check);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("달인 승인 완료", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(ApiResponse.error("달인 승인 실패", "BAD_REQUEST"));
        }

    }

    
    // 고수 취소 버튼 클릭시
    @PostMapping("/admin/pro/cancel/{member_no}")
    public ResponseEntity<ApiResponse> cancelMember(@PathVariable("member_no") long member_no) {
    	// 이메일 검사
        String email = memberService.getMemberEmail(member_no);
        boolean check = false;
        log.info("email : " + email);
        
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("이메일이 유효하지 않습니다.", "BAD_REQUEST"));
        }
        
        // 카카오/구글/네이버 회원 가입 한 사람들
        if (email.contains(" ")) {
            // 이메일이 유효하지 않으면 상태값만 변경
        	check = true;
            boolean result = adminService.cancelMember(member_no , check);
            if (result) {
                return ResponseEntity.ok(ApiResponse.success("달인 취소 완료 (이메일 전송 생략)", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body(ApiResponse.error("달인 취소 실패", "BAD_REQUEST"));
            }
        }

        // 이메일 전송
        try {
            mailService.cancelMail(email); // 이메일 전송
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(ApiResponse.error("이메일 전송 실패", "INTERNAL_SERVER_ERROR"));
        }
        
    	// member_status 상태 변경
        boolean result = adminService.cancelMember(member_no, check);
        if (result) {
        	return ResponseEntity.ok(ApiResponse.success("달인 취소 완료", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(ApiResponse.error("달인 취소 실패", "BAD_REQUEST"));
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
    
    
    // 일반 회원 조회 ( 페이징 처리 )
    @GetMapping("/admin/member/user")
    public ResponseEntity<Page<Member>> getUserMembers(
    		@RequestParam(value = "page", defaultValue = "1") int page, 
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Member> userData = adminService.getUserMembers(pageable);
        return ResponseEntity.ok(userData);
    }

    // 고수 회원 조회 ( 페이징 처리 )
    @GetMapping("/admin/member/pro")
    public ResponseEntity<Page<ProDTO>> getProMembersWithDetails(
    		@RequestParam(value = "page", defaultValue = "1") int page, 
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page -1, size);
        Page<ProDTO> proMembers = adminService.getProMembersWithDetails(pageable);
        return ResponseEntity.ok(proMembers);
    }

    // 탈퇴 회원 조회 ( 페이징 처리 )
    @GetMapping("/admin/member/cancel")
    public ResponseEntity<Page<CancelDTO>> getCancelMembersWithDetails(
    		@RequestParam(value = "page", defaultValue = "1") int page, 
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page -1, size);
        Page<CancelDTO> cancelMembers = adminService.getCancelMembersWithDetails(pageable);
        return ResponseEntity.ok(cancelMembers);
    }

    
    // 이벤트 및 공지 페이지 조회
    @GetMapping("/admin/article")
    public List<ArticleImageDTO> getArticlesWithImages() {
        return adminService.getArticles();
    }
    
    // 이벤트 및 공지 등록
    @PostMapping("/admin/article/write")
    public ResponseEntity<String> writeArticle(
            @ModelAttribute ArticleImageDTO articleImageDTO,  // 텍스트 필드를 ArticleDTO로 처리
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication) { // 파일만 별도로 처리
    	
    	//memberNo 토큰값에서 가져옴
    	articleImageDTO.setMemberNo(memberService.getMemberNo(authentication.getName()));
    	System.out.println("images : " + images);
        try {
            // ArticleDTO에 이미지 파일 리스트 설정
        	articleImageDTO.setImageFiles(images == null ? List.of() : images);
        	
            boolean result = adminService.writeArticle(articleImageDTO);
            
            if (result) {
	            return ResponseEntity.ok("게시글이 성공적으로 작성되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("게시글 작성 중 오류가 발생했습니다.");
	        }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    // 공지 및 게시글 수정
    @PutMapping("/admin/article/update/{articleNo}")
    public ResponseEntity<String> updateArticle(
            @PathVariable("articleNo") Long articleNo,  // URL 경로에서 게시글 번호를 받습니다.
            @ModelAttribute ArticleImageDTO articleImageDTO,  // 수정할 게시글 정보
            @RequestPart(value = "images", required = false) List<MultipartFile> images,  // 수정시 추가될 이미지 파일
            @RequestParam(value = "removeImages", required = false) List<String> removeImages, // 삭제할 이미지 UUID 리스트
            Authentication authentication) { // 파일만 별도로 처리

        System.out.println("글 번호 : " + articleNo);
        System.out.println("articleNo (null 체크) : " + (articleNo == null ? "null" : articleNo));

        // memberNo 토큰값에서 가져옴
        articleImageDTO.setMemberNo(memberService.getMemberNo(authentication.getName()));
        articleImageDTO.setArticleNo(articleNo);  // 경로 변수로 받은 articleNo를 DTO에 설정

        try {
            // ArticleDTO에 이미지 파일 리스트 설정
            articleImageDTO.setImageFiles(images == null ? new ArrayList<>() : images);  // 새로 추가된 이미지
            articleImageDTO.setRemoveImageUuidNames(removeImages == null ? new ArrayList<>() : removeImages);  // 삭제할 이미지 UUID 설정

            // 이미지를 처리하는 서비스 호출
            boolean result = adminService.updateArticle(articleImageDTO);

            if (result) {
                return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("게시글 수정 중 오류가 발생했습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    
    // 공지 및 이벤트 게시글 삭제
    @DeleteMapping("/admin/article/delete/{articleNo}")
    public ResponseEntity<String> deleteArticle(
            @PathVariable("articleNo") Long articleNo,
            Authentication authentication) {

        System.out.println("삭제할 글 번호 : " + articleNo);
        System.out.println("articleNo (null 체크) : " + (articleNo == null ? "null" : articleNo));

        try {
            // memberNo 토큰값에서 가져옴
            Long memberNo = memberService.getMemberNo(authentication.getName());

            // 게시글 삭제 서비스 호출
            boolean result = adminService.deleteArticle(articleNo, memberNo);

            if (result) {
                return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("게시글 삭제 중 오류가 발생했습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }


    
   
    

    
}
