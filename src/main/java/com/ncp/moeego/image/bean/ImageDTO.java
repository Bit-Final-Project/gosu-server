package com.ncp.moeego.image.bean;

import lombok.Data;

@Data
public class ImageDTO {
    private Long imageNo;         // 이미지 번호
    private Long reviewNo;        // 리뷰 번호
    private Long proItemNo;    // 프로 게시판 글 번호
    private Long articleNo;       // 게시판 글 번호
    private Long memberNo;        // 사용자 번호
    private String imageName;     // 원본 이미지 이름
    private String imageUuidName; // UUID로 저장된 이미지 이름

    // 기본 생성자
    public ImageDTO() {
    }

    public ImageDTO(Long imageNo, Long reviewNo, Long proItemNo, Long articleNo, Long memberNo,
                    String imageName, String imageUuidName) {
        this.imageNo = imageNo;
        this.reviewNo = reviewNo;
        this.proItemNo = proItemNo;
        this.articleNo = articleNo;
        this.memberNo = memberNo;
        this.imageName = imageName;
        this.imageUuidName = imageUuidName;
    }
}
