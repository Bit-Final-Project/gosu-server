package com.ncp.moeego.image.bean;

import com.ncp.moeego.article.bean.Article;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.pro.article.bean.ProArticle;
import com.ncp.moeego.pro.review.bean.Review;

@Entity
@Data
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_no")
	private Long imageNo;
	
	@ManyToOne // 여러개의 이미지를 한 리뷰에 넣을수있음
	@JoinColumn(name="review_no")
	private Review reviewNo;
	
	@ManyToOne // 여러개의 이미지를 고수 게시판에 넣을수있음
	@JoinColumn(name="pro_article_no")
	private ProArticle proArticleNo;

	@ManyToOne // 여러개의 이미지를 게시판에 넣을수있음
	@JoinColumn(name="article_no")
	private Article articleNo;
	
	@ManyToOne // 여러개의 이미지를 사용자가 넣을수있음
	@JoinColumn(name="member_no", nullable = false)
	private Member memberNo;
	
	@Column(length = 3000)
	private String imageName;
	
}
