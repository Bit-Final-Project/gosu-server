package com.ncp.moeego.member.bean;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleImageDTO {
    private Long articleNo;
    private String subject;
    private String content;
    private int type;
    private Long imageNo;
    private String imageUuidName;
    private LocalDateTime write_date;

    public ArticleImageDTO() {}
    
    public ArticleImageDTO(Long articleNo, String subject, String content, Integer type, Long imageNo, String imageUuidName, LocalDateTime write_date) {
        this.articleNo = articleNo;
        this.subject = subject;
        this.content = content;
        this.type = type;
        this.imageNo = imageNo;
        this.imageUuidName = imageUuidName;
        this.write_date = write_date;
    }
}
