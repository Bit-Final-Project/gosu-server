package com.ncp.moeego.member.repository;

import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //email(id)로 중복 확인
    boolean existsByEmail(String email);

    //이름으로 전체 회원 검색
    List<Member> findAllByNameContaining(String name);

    //username을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Member findByEmail(String email);
	
    
    // 전체 회원 수 , 탈퇴 회원 수 ( 관리자 대시보드 차트 데이터화 )
    int countByMemberStatus(MemberStatus memberStatus);

    // 상태별 회원 리스트 반환
    @Query("SELECT new com.ncp.moeego.member.bean.MemberSummaryDTO(" +
            "m.memberNo, m.name, p.oneIntro, COUNT(p.depriveDate)) " +
            "FROM Member m " +
            "LEFT JOIN m.pro p " +
            "WHERE m.memberStatus = :status " +
            "GROUP BY m.memberNo, m.name, p.oneIntro")
    List<MemberSummaryDTO> findMemberSummaryByStatus(MemberStatus status);

    
    
}
