package com.ncp.moeego.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ncp.moeego.image.bean.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

	List<Image> findByArticleNo_ArticleNo(Long articleNo);



}