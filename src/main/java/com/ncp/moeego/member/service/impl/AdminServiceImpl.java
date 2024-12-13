package com.ncp.moeego.member.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ncp.moeego.member.bean.CancelDTO;
import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.ProDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.AdminService;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.repository.ProRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final ProRepository proRepository;

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
	public List<MemberSummaryDTO> getPendingProMembers() {
        return memberRepository.findMemberSummaryByStatus(MemberStatus.ROLE_PEND_PRO);
    }

	@Override
	public boolean approveMember(Long member_no) {
	    Member member = memberRepository.findById(member_no).orElse(null);
	    if (member != null && member.getMemberStatus() == MemberStatus.ROLE_PEND_PRO) {
	        member.setMemberStatus(MemberStatus.ROLE_PRO);  // 멤버 상태 변경
	        memberRepository.save(member);  // 변경된 멤버 저장

	        // Pro 엔티티의 accessDate 값도 오늘 날짜로 설정
	        Pro pro = proRepository.findByMember(member);  // 해당 멤버의 Pro 객체 찾기
	        if (pro != null) {
	            // 원하는 포맷 (예: "2024-12-06 14:28:25")
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	            String formattedDate = LocalDateTime.now().format(formatter);  // 현재 시간 포맷팅
	            LocalDateTime accessDate = LocalDateTime.parse(formattedDate, formatter);  // 다시 LocalDateTime으로 변환

	            pro.setAccessDate(accessDate);  // accessDate를 포맷된 날짜로 설정
	            proRepository.save(pro);  // Pro 엔티티 저장
	        }

	        return true;
	    }
	    return false;
	}
	
	@Override
	public boolean cancelMember(Long member_no) {
	    Member member = memberRepository.findById(member_no).orElse(null);
	    if (member != null && member.getMemberStatus() == MemberStatus.ROLE_PEND_PRO) {
	        member.setMemberStatus(MemberStatus.ROLE_USER);  // 예: ROLE_USER로 변경
	        memberRepository.save(member);
	        return true;
	    }
	    return false;
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
	
	// 일반 회원 조회
    public List<Member> getUserMembers() {
        return memberRepository.findAllUser();
    }

    // 고수 회원 조회
    public List<ProDTO> getProMembersWithDetails() {
        return memberRepository.findProMembersWithDetails();
    }

	@Override
	public List<CancelDTO> getCancelMembersWithDetails() {
        return memberRepository.findAllCancelDetails();
    }


	
}

	

