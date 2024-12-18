package com.ncp.moeego.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ncp.moeego.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	@Query("SELECT r.reviewNo, r, r.star, " +  // reviewNo, star : 리뷰, 별점 가져오기
			" p.member.name AS proName, " + // proName: 프로 이름 가져오기 (Pro 테이블에서 member 필드의 name을 가져옴)
			" pi.subject AS subject, " + // subject: 서비스 이름 가져오기 (ProItem 테이블에서 subject 필드를 가져옴)
			" m.name AS memberName " + // memberName: 작성자 이름 가져오기 (Member 테이블에서 name을 가져옴)
			" FROM Review r " + // Review 테이블을 기준으로 조회
			" JOIN ProItem pi ON r.proItem.proItemNo = pi.proItemNo " + // Review와 ProItem 조인
			" JOIN Pro p ON pi.pro.proNo = p.proNo " + // ProItem과 Pro 조인
			" JOIN Member m ON r.member.memberNo = m.memberNo " + // Review와 Member 조인
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



}
