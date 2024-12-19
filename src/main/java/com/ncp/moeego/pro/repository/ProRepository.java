package com.ncp.moeego.pro.repository;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.pro.dto.FavoriteResponse;
import com.ncp.moeego.pro.dto.ItemResponse;
import com.ncp.moeego.pro.entity.Pro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProRepository extends JpaRepository<Pro, Long> {

    @Query(
            """
                    SELECT new com.ncp.moeego.pro.dto.FavoriteResponse(
                                p.proNo, p.member.name, p.member.profileImage,p.mainCategory.mainCateNo,
                                p.star, p.oneIntro
                                )
                    From Pro p
                    Where p.proNo IN :proNo
                    
                    """
    )
    Page<FavoriteResponse> findByProNoIn(@Param("proNo") List<Long> proNo, Pageable pageable);

    Pro findByMember(Member member);

    @Query("""
            select distinct p from Pro p
            left join p.proItems pi
            where p.member.memberStatus = :memberStatus
            and (:location is null or p.member.address like %:location%)
            and (:subCateNo is null or pi.subCategory.subCateNo = :subCateNo)
            """)
    Page<Pro> findFilteredPros(@Param("memberStatus") MemberStatus memberStatus, Pageable pageable, @Param("subCateNo") Long subCateNo, @Param("location") String location);

    @Query("""
            select p from Pro p
            left join p.member m
            where p.member.memberStatus = 'ROLE_PRO' 
            and (:value is null or m.name like %:value%)
            or (:value is null or p.intro like %:value%)
            or (:value is null or p.oneIntro like %:value%)
            """)
    Page<Pro> findSearchValue(Pageable pageable, @Param("value") String value);

	Optional<Pro> findByMemberMemberNo(Long memberNo);

	Pro findByMember_MemberNo(Long memberNo);

	// Pro 업데이트
	@Modifying
	@Query("UPDATE Pro p SET p.reviewCount = :totalReviewCount, p.star = :averageStar WHERE p.proNo = :proNo")
	void updateStatistics(@Param("proNo") Long proNo, @Param("totalReviewCount") int totalReviewCount, @Param("averageStar") float averageStar);

}
