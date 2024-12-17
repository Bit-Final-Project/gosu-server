package com.ncp.moeego.member.repository;

import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.bean.CancelDTO;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.ProDTO;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.pro.entity.Pro;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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


    // 모든 일반 회원 조회
    @Query("SELECT m FROM Member m")
    List<Member> findAllUser();  // 일반 회원 조회

    @Query("SELECT DISTINCT new com.ncp.moeego.member.bean.ProDTO(" +
            "p.member.memberNo, " +
            "p.member.name, " +
            "p.accessDate, " +
            "p.star, " +
            "p.depriveDate, " +
            "p.proNo, " +
            "p.mainCategory.mainCateName, " +  
            "p.oneIntro, " +                   
            "p.intro) " +                      
            "FROM Pro p " +
            "WHERE p.mainCategory.mainCateName IS NOT NULL")
    List<ProDTO> findProMembersWithDetails();

    // 탈퇴 회원 조회
    @Query("SELECT new com.ncp.moeego.member.bean.CancelDTO(m.name, m.phone, m.email , c.cancelNo, c.cancelDate, c.reason) " +
            "FROM Cancel c JOIN c.memberNo m")
     List<CancelDTO> findAllCancelDetails();

    // 회원 프로필 이미지 업로드
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.profileImage = :cloudKey WHERE m = :member")
    void updateProfileImage(@Param("member") Member member, @Param("cloudKey") String cloudKey);
    // 회원 프로필 이미지 삭제
    
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.profileImage = null WHERE m.memberNo = :memberNo")
    void updateProfileImageToNull(@Param("memberNo") Long memberNo);
    
}
