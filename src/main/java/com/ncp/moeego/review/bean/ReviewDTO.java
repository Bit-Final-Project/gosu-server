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
    private Long proItemNo;            // ProItem의 ID
    private Long mainCateNo;       // MainCategory의 ID
    private Long memberNo;             // Member의 ID
    private String reviewContent;      // 리뷰 내용
    private float star;                // 별점 (0~5.0)
    private LocalDateTime writeDate;   // 작성일
    private List<MultipartFile> imageFiles;

    // 기본 생성자
    public ReviewDTO() {
    	
    }

    // 생성자
    public ReviewDTO(Long reviewNo, Long proItemNo, Long mainCateNo, Long memberNo, 
                     String reviewContent, float star, LocalDateTime writeDate, List<MultipartFile> imageFiles) {
        this.reviewNo = reviewNo;
        this.proItemNo = proItemNo;
        this.mainCateNo = mainCateNo;
        this.memberNo = memberNo;
        this.reviewContent = reviewContent;
        this.star = star;
        this.writeDate = writeDate;
        this.imageFiles = imageFiles;
    }
}
