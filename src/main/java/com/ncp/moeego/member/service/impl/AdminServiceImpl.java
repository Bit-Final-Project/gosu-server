package com.ncp.moeego.member.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ncp.moeego.member.bean.ArticleImageDTO;
import com.ncp.moeego.member.bean.CancelDTO;
import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.article.repository.ArticleRepository;
import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.ProDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.AdminService;
import com.ncp.moeego.pro.entity.ItemStatus;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.entity.ProItem;
import com.ncp.moeego.pro.repository.ProItemRepository;
import com.ncp.moeego.pro.repository.ProRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final ProRepository proRepository;
    private final ProItemRepository proItemRepository;
    private final ArticleRepository articleRepository;

    @Override
    public int getRoleUserCount() {
        return memberRepository.countByMemberStatus(MemberStatus.ROLE_USER);
    }

    @Override
    public int getRoleCancelCount() {
        return memberRepository.countByMemberStatus(MemberStatus.ROLE_CANCEL);
    }

	@Override
	public int getRoleProCount() {
		return memberRepository.countByMemberStatus(MemberStatus.ROLE_PRO);
	}

	@Override
	public List<MemberSummaryDTO> getPendingProMembers(MemberStatus status) {
	    return memberRepository.findMemberSummaryByStatus(status);
	}

	@Override
	public boolean approveMember(Long member_no) {
	    // 1. Member 테이블에서 멤버 조회
	    Member member = memberRepository.findById(member_no).orElse(null);
	    if (member != null && member.getMemberStatus() == MemberStatus.ROLE_PEND_PRO) {
	        
	        // 2. Member 테이블에서 상태를 ROLE_PRO로 변경
	        member.setMemberStatus(MemberStatus.ROLE_PRO);
	        memberRepository.save(member);  // 변경된 멤버 저장

	        // 3. Pro 엔티티의 accessDate 값도 현재 날짜로 설정
	        Pro pro = proRepository.findByMember(member);  // 해당 멤버의 Pro 객체 찾기
	        if (pro != null) {
	            pro.setAccessDate(LocalDateTime.now());  // accessDate를 현재 날짜로 설정
	            proRepository.save(pro);  // Pro 엔티티 저장
	        }

	        // 4. ProItem 테이블에서 상태를 ACTIVE로 변경
	        List<ProItem> proItems = proItemRepository.findByPro(pro);  // 해당 Pro에 관련된 모든 ProItem 찾기
	        for (ProItem proItem : proItems) {
	            proItem.setItemStatus(ItemStatus.ACTIVE);  // 상태를 ACTIVE로 설정
	            proItemRepository.save(proItem);  // ProItem 상태 저장
	        }

	        return true;
	    }
	    return false;
	}
	
	@Override
	public boolean cancelMember(Long memberNo) {
	    // Member 객체 가져오기
	    Member member = memberRepository.findById(memberNo).orElse(null);

	    if (member == null) {
	        return false; // Member가 없으면 취소 실패
	    }

	    // Pro 조회 (Member와 연결된 Pro 데이터)
	    Pro pro = proRepository.findByMember_MemberNo(memberNo);

	    if (pro != null) {
	        // ProItem 삭제 (Pro와 연결된 모든 ProItem 삭제)
	        List<ProItem> proItems = proItemRepository.findByPro_ProNo(pro.getProNo());
	        if (!proItems.isEmpty()) {
	            proItemRepository.deleteAll(proItems);
	        }

	        // Pro 삭제
	        proRepository.delete(pro);
	    }

	    // Member 상태 변경
	    member.setMemberStatus(MemberStatus.ROLE_USER); // 상태 변경
	    memberRepository.save(member); // Member 상태 변경 후 저장

	    return true; // 작업 성공
	}


	@Override
	public List<Map<String, Object>> getWeekMemberData() {
		LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);

        LocalDateTime startDateTime = weekAgo.atStartOfDay();  // 일주일 전 자정
        LocalDateTime endDateTime = today.atStartOfDay().plusDays(1).minusNanos(1);
        
        // 회원 가입 데이터 조회 (가입 날짜가 일주일 이내인 회원들)
        List<Member> members = memberRepository.findByJoinDateBetween(startDateTime, endDateTime);

        // 날짜별로 가입 수 집계
        return members.stream()
                .collect(Collectors.groupingBy(member -> member.getJoinDate().toLocalDate()))
                .entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> result = Map.of(
                        "date", entry.getKey().format(DateTimeFormatter.ISO_DATE),
                        "count", entry.getValue().size()
                    );
                    return result;
                })
                .collect(Collectors.toList());
	}

	@Override
	public List<Map<String, Object>> getProMemberJoinData(LocalDateTime startDateTime, LocalDateTime endDateTime) {
	    List<Pro> proMembers = memberRepository.findProMembersByJoinDate(startDateTime, endDateTime);

	    return proMembers.stream()
	        .collect(Collectors.groupingBy(
	            member -> member.getAccessDate().toLocalDate(), // 날짜별로 그룹핑
	            Collectors.counting() // 각 날짜별 개수 계산
	        ))
	        .entrySet()
	        .stream()
	        .map(entry -> {
	            Map<String, Object> result = Map.of(
	                "date", entry.getKey().format(DateTimeFormatter.ISO_DATE),  // yyyy-MM-dd 형식으로 변환
	                "count", entry.getValue()
	            );
	            return result;
	        })
	        .collect(Collectors.toList());
	}

	public List<Map<String, Object>> getCancelledMemberData(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 날짜 범위에 해당하는 탈퇴한 멤버 조회
        List<Cancel> cancelledMembers = memberRepository.findByCancelDateBetween(startDateTime, endDateTime);

        // 날짜별로 탈퇴한 멤버 수 집계
        return cancelledMembers.stream()
            .collect(Collectors.groupingBy(
                cancel -> cancel.getCancelDate().toLocalDate(), // 날짜별로 그룹핑
                Collectors.counting() // 각 날짜별 개수 계산
            ))
            .entrySet()
            .stream()
            .map(entry -> {
                Map<String, Object> result = Map.of(
                    "date", entry.getKey().format(DateTimeFormatter.ISO_DATE),  // yyyy-MM-dd 형식으로 변환
                    "count", entry.getValue()
                );
                return result;
            })
            .collect(Collectors.toList());
    }

	// 박탈 버튼 클릭시
	@Override
	public void revokeMember(Long memberNo) {
	    // 1. 회원 정보 조회
	    Member member = memberRepository.findById(memberNo)
	        .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
	    
	    // 2. member_status를 'ROLE_CANCEL_PRO'로 변경
	    member.setMemberStatus(MemberStatus.ROLE_CANCEL_PRO);
	    memberRepository.save(member);

	    // 3. pro 테이블에서 해당 member_no에 연관된 pro 조회
	    Pro pro = proRepository.findByMemberMemberNo(memberNo)
	        .orElseThrow(() -> new IllegalArgumentException("프로 회원 정보가 존재하지 않습니다."));

	    // 4. pro_item 테이블에서 해당 pro_no에 연관된 모든 pro_item 삭제
	    List<ProItem> proItems = proItemRepository.findByPro(pro);  // pro와 연관된 pro_items 조회
	    proItemRepository.deleteAll(proItems);  // 연관된 pro_item 삭제

	    // 5. pro 테이블에서 해당 member_no에 연관된 pro 삭제
	    proRepository.delete(pro);  // pro 삭제
	}

	
	// 일반 회원 조회
    public List<Member> getUserMembers() {
        return memberRepository.findAllUser();
    }

    // 고수 회원 조회
    public List<ProDTO> getProMembersWithDetails() {
        return memberRepository.findProMembersWithRolePro();
    }

    // 탈퇴 회원 조회
	@Override
	public List<CancelDTO> getCancelMembersWithDetails() {
        return memberRepository.findAllCancelDetails();
    }

	// 이벤트 및 공지 페이지 조회
	public List<ArticleImageDTO> getArticlesWithImages() {
        return memberRepository.findArticlesWithImages();
    }
	
	
}

	

