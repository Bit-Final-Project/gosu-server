package com.ncp.moeego.article.bean;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ArticleDTO {
	private Long articleNo;
	private Long memberNo; // Member 엔티티 대신 Member의 ID만 사용
	private String subject;
	private String content;
	private int view;
	private int type;
	private LocalDateTime writeDate;
	private int likes;
	private String elapsedTime;
	private String memberName;
	private String service;
	private String area;
	private int commentCount;

	
	// 기본 생성자
	public ArticleDTO() {
		
	}

	// DTO 객체 반환 생성자 추가
	public ArticleDTO(Long articleNo, String subject, String content, int view, int type, LocalDateTime writeDate,
			Long memberNo, int likes, String elapsedTime, String memberName, String service, String area, int commentCount) {
		this.articleNo = articleNo;
		this.subject = subject;
		this.content = content;
		this.view = view;
		this.type = type;
		this.writeDate = writeDate;
		this.memberNo = memberNo;
		this.likes = likes;
		this.elapsedTime = elapsedTime;
		this.memberName = memberName;
		this.service = service;
		this.area = area;
		this.commentCount = commentCount;
	}

}
