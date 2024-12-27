package com.ncp.moeego.review.repository;

import java.util.List;

import com.ncp.moeego.review.bean.ItemReviewResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ncp.moeego.review.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	@Query("SELECT r.reviewNo, r, r.star, " +  // reviewNo, star : 리뷰, 별점 가져오기
	        " p.member.name AS proName, " + // proName: 프로 이름 가져오기 (Pro 테이블에서 member 필드의 name을 가져옴)
	        " pi.subject AS subject, " + // subject: 서비스 이름 가져오기 (ProItem 테이블에서 subject 필드를 가져옴)
	        " m.name AS memberName, " + // memberName: 작성자 이름 가져오기 (Member 테이블에서 name을 가져옴)
	        " m.profileImage AS profileImage " + // profileImage: 프로필 이미지 가져오기 (Member 테이블에서 profileImage 가져오기)
	        " FROM Review r " + // Review 테이블을 기준으로 조회
	        " JOIN ProItem pi ON r.proItem.proItemNo = pi.proItemNo " + // Review와 ProItem 조인
	        " JOIN Pro p ON pi.pro.proNo = p.proNo " + // ProItem과 Pro 조인
	        " JOIN Member m ON p.member.memberNo = m.memberNo " + // Pro 테이블과 Member 테이블을 조인하여 profileImage 가져오기
	        " ORDER BY r.writeDate DESC")
	Page<Object[]> findReviewsWithDetails(PageRequest pageRequest);


    // 내가 작성한 리뷰 조회
	@Query("SELECT r.reviewNo, r, r.star, " +  // reviewNo, star : 리뷰, 별점 가져오기
	        " p.member.name AS proName, " + // proName: 프로 이름 가져오기 (Pro 테이블에서 member 필드의 name을 가져옴)
	        " pi.subject AS subject, " + // subject: 서비스 이름 가져오기 (ProItem 테이블에서 subject 필드를 가져옴)
	        " m.name AS memberName " + // memberName: 작성자 이름 가져오기 (Member 테이블에서 name을 가져옴)
	        " FROM Review r " + // Review 테이블을 기준으로 조회
	        " JOIN ProItem pi ON r.proItem.proItemNo = pi.proItemNo " + // Review와 ProItem 조인
	        " JOIN Pro p ON pi.pro.proNo = p.proNo " + // ProItem과 Pro 조인
	        " JOIN Member m ON r.member.memberNo = m.memberNo " + // Review와 Member 조인
	        " WHERE r.member.memberNo = :memberNo " + // memberNo를 조건으로 추가
	        " ORDER BY r.writeDate DESC")
	Page<Object[]> findReviewsByMemberNo(@Param("memberNo") Long memberNo, Pageable pageable);

	// 전체 데이터 조회시 리뷰 번호에 맞는 이미지 조회
	@Query("SELECT i.imageUuidName FROM Image i WHERE i.review.reviewNo = :reviewNo")
	List<String> findImageUuidsByReviewNo(@Param("reviewNo") Long reviewNo);

	// ProItem의 리뷰 개수
	@Query("SELECT COUNT(r) FROM Review r WHERE r.proItem.proItemNo = :proItemNo")
	int countByProItemNo(@Param("proItemNo") Long proItemNo);

	// ProItem의 star 합계 (null일 경우 0 반환)
	@Query("SELECT COALESCE(SUM(r.star), 0) FROM Review r WHERE r.proItem.proItemNo = :proItemNo")
	float sumStarByProItemNo(@Param("proItemNo") Long proItemNo);


	@Query("""
			select new com.ncp.moeego.review.bean.ItemReviewResponse(
			r.reviewNo,r.reviewContent, r.star, r.writeDate, r.proItem.pro.member.name, r.proItem.subject, r.member.name, r.proItem.proItemNo, r.member.memberNo
			) from Review r
			where r.proItem.proItemNo = :proItemNo
			""")
	Page<ItemReviewResponse> findReviewsByProItem_ProItemNo(@Param("proItemNo") Long proItemNo, Pageable pageable);


}
