package com.ncp.moeego.review.bean;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long reviewNo;             // 리뷰 번호
    private String reviewContent;      // 리뷰 내용
    private float star;                // 별점 (0~5.0)
    private LocalDateTime writeDate;   // 작성일
    private String proName;       // 달인 이름
    private String subject;       // 서비스 이름
    private String memberName;    // 리뷰 작성자
    private String elapsedTime;  // 경과 시간 (서비스 계층에서 계산)
    private Long proItemNo;            // ProItem의 ID
    private Long memberNo;             // Member의 ID
    private List<MultipartFile> imageFiles;

    // 기본 생성자
    public ReviewDTO() {
    	
    }
    
    // 전체 리뷰 조회 생성자
    public ReviewDTO(Long reviewNo, String proName,float star, String subject, String reviewContent, String memberName, LocalDateTime writeDate, String elapsedTime) {
        this.reviewNo = reviewNo;
    	this.proName = proName;
        this.star = star;
    	this.subject = subject;
        this.reviewContent = reviewContent;
        this.memberName = memberName;
        this.writeDate = writeDate;
        this.elapsedTime = elapsedTime;
    }
    
    
}
