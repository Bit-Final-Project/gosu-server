package com.ncp.moeego.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ncp.moeego.image.bean.Image;

import jakarta.transaction.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

	List<Image> findByArticleNo_ArticleNo(Long articleNo);

	// 프로필 등록시 이미 있다면 삭제
	@Modifying
	@Transactional
	@Query("DELETE FROM Image i WHERE i.imageUuidName = :profileImage")
	void deleteByImageUuidName(@Param("profileImage") String profileImage);



}
