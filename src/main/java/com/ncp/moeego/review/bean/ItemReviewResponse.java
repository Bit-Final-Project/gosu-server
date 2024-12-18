package com.ncp.moeego.review.bean;

import com.ncp.moeego.common.ConvertDate;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemReviewResponse {
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
    private List <String> imageUuidNames; //

    public ItemReviewResponse(Long reviewNo, String reviewContent, float star, LocalDateTime writeDate, String proName, String subject, String memberName, Long proItemNo, Long memberNo) {
        this.reviewNo = reviewNo;
        this.reviewContent = reviewContent;
        this.star = star;
        this.writeDate = writeDate;
        this.proName = proName;
        this.subject = subject;
        this.memberName = memberName;
        this.proItemNo = proItemNo;
        this.memberNo = memberNo;
        this.elapsedTime = ConvertDate.calculateDate(writeDate);
    }
}
