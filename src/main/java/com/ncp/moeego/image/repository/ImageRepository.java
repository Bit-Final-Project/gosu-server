package com.ncp.moeego.image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.image.entity.Image;
import com.ncp.moeego.member.entity.Member;

import jakarta.transaction.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

	List<Image> findByArticle_ArticleNo(Long articleNo);

	// 프로필 등록시 이미 있다면 삭제
	@Modifying
	@Transactional
	@Query("DELETE FROM Image i WHERE i.imageUuidName = :profileImage")
	void deleteByImageUuidName(@Param("profileImage") String profileImage);

	// uuid값 가져오기
	
	// 게시글 번호 기준으로 이미지 리스트 조회
	List<Image> findByArticle(Article article);

	Optional<Image> findByMember(Optional<Member> member);

}
