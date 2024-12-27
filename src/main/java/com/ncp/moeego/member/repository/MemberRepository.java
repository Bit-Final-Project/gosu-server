package com.ncp.moeego.member.repository;

import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.image.entity.Image;
import com.ncp.moeego.member.bean.*;
import com.ncp.moeego.member.bean.MemberSummaryDTO;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.entity.ProItem;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "m.memberNo, m.name, m.memberStatus, p.oneIntro, p.intro , p.mainCategory.mainCateName) " +
            "FROM Member m " +
            "LEFT JOIN Pro p ON p.member = m " +
            "WHERE m.memberStatus = :status " +
            "GROUP BY m.memberNo, m.name, m.memberStatus, p.oneIntro, p.mainCategory.mainCateName")
    List<MemberSummaryDTO> findMemberSummaryByStatus(Pageable pageable , @Param("status") MemberStatus status);
    
    // 일주일 치 회원 가입 수
    List<Member> findByJoinDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 일주일 간 고수 등록 수
    @Query("SELECT p FROM Pro p WHERE p.accessDate BETWEEN :startDate AND :endDate")
    List<Pro> findProMembersByJoinDate(@Param("startDate") LocalDateTime startDateTime, @Param("endDate") LocalDateTime endDateTime);

    // 일주일 간 탈퇴 회원 수
    @Query("SELECT p FROM Cancel p WHERE p.cancelDate BETWEEN :startDate AND :endDate")
    List<Cancel> findByCancelDateBetween(@Param("startDate") LocalDateTime startDateTime, @Param("endDate") LocalDateTime endDateTime);

    //고수 카운트 리스트
    @Query("SELECT new com.ncp.moeego.member.bean.ProCountDTO(p.mainCategory.mainCateNo, p.mainCategory.mainCateName, count(p.proNo)) " +
            "FROM Pro p " +
            "GROUP BY p.mainCategory.mainCateNo, p.mainCategory.mainCateName")
    List<ProCountDTO> findByProCountList();

    // 일반 회원 조회 (memberStatus = 'ROLE_USER' 기준)
    @Query("SELECT m FROM Member m WHERE m.memberStatus = 'ROLE_USER'")
    Page<Member> findUserMembers(Pageable pageable);

    // 고수 회원 조회 (memberStatus = 'ROLE_PRO' 기준)
    @Query("SELECT DISTINCT new com.ncp.moeego.member.bean.ProDTO( "
            + "p.member.memberNo, "
            + "p.member.name, "
            + "p.accessDate, "
            + "p.star, "
            + "p.proNo, "
            + "p.mainCategory.mainCateName, "
            + "p.oneIntro, "
            + "p.intro, "
            + "p.member.memberStatus) "
        + "FROM Pro p "
        + "JOIN p.member m "
        + "LEFT JOIN p.proItems pi "
        + "LEFT JOIN p.mainCategory mc "
        + "WHERE m.memberStatus = 'ROLE_PRO' "
        + "AND pi.itemStatus = 'ACTIVE' "
        + "AND mc.mainCateName IS NOT NULL")
    Page<ProDTO> findProMembersWithRolePro(Pageable pageable);

    // 탈퇴 회원 조회 (memberStatus = 'ROLE_CANCEL' 기준)
    @Query("SELECT new com.ncp.moeego.member.bean.CancelDTO(m.name, m.phone, m.email , c.cancelNo, c.cancelDate, c.reason) " +
            "FROM Cancel c JOIN c.member m WHERE m.memberStatus = 'ROLE_CANCEL'")
    Page<CancelDTO> findCancelledMembers(Pageable pageable);

    // 회원 프로필 이미지 업로드
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.profileImage = :cloudKey WHERE m = :member")
    void updateProfileImage(@Param("member") Member member, @Param("cloudKey") String cloudKey);
    

    // 회원 프로필 이미지 업로드 시 게시글 작성해도 하나만 가져오기
    @Query("SELECT m FROM Member m " +
    	       "LEFT JOIN Image i ON i.member = m " +
    	       "WHERE m.memberNo = :memberNo " +
    	       "AND (i.article IS NULL AND i.review IS NULL AND i.proItem IS NULL)")
    Optional<Member> findByIdWithEmptyImage(@Param("memberNo") Long memberNo);
    
    // 회원 프로필 이미지 삭제
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.profileImage = null WHERE m.memberNo = :memberNo")
    void updateProfileImageToNull(@Param("memberNo") Long memberNo);

    // 메일 상태 값 가져오기
    @Query("SELECT m.emailStatus FROM Member m WHERE m.email = :username ")
	Integer findEmailStatusByName(@Param("username") String username);


    
}
