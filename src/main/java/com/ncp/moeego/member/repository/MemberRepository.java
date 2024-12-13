package com.ncp.moeego.member.repository;

import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.pro.entity.Pro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //email(id)로 중복 확인
    boolean existsByEmail(String email);

    //이름으로 전체 회원 검색
    List<Member> findAllByNameContaining(String name);

    //username을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Optional<Member> findByEmail(String email);

    // 전체 회원 수 , 탈퇴 회원 수 ( 관리자 대시보드 차트 데이터화 )
    int countByMemberStatus(MemberStatus memberStatus);

    // 상태별 회원 리스트 반환
    @Query("SELECT new com.ncp.moeego.member.bean.MemberSummaryDTO(" +
            "m.memberNo, m.name, m.memberStatus, p.oneIntro, COUNT(p.depriveDate)) " +  
            "FROM Member m " +
            "LEFT JOIN Pro p ON p.member = m " +
            "WHERE m.memberStatus = :status " +
            "GROUP BY m.memberNo, m.name, m.memberStatus, p.oneIntro")
    List<MemberSummaryDTO> findMemberSummaryByStatus(@Param("status") MemberStatus status);
    
    // 일주일 치 회원 가입 수
    List<Member> findByJoinDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 일주일 간 고수 등록 수
    @Query("SELECT p FROM Pro p WHERE p.accessDate BETWEEN :startDate AND :endDate")
    List<Pro> findProMembersByJoinDate(@Param("startDate") LocalDateTime startDateTime, @Param("endDate") LocalDateTime endDateTime);

    // 일주일 간 탈퇴 회원 수
    @Query("SELECT p FROM Cancel p WHERE p.cancelDate BETWEEN :startDate AND :endDate")
    List<Cancel> findByCancelDateBetween(@Param("startDate") LocalDateTime startDateTime, @Param("endDate") LocalDateTime endDateTime);

}
