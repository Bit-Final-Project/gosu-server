package com.ncp.moeego.comment.dto;

import com.ncp.moeego.comment.entity.CommentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MemberCommentResponse {
    private Long commentNo;
    private Long articleNo;
    private String articleSubject;
    private String content;
    private CommentStatus commentStatus;
    private LocalDateTime writeDate;

    public MemberCommentResponse(Long commentNo, Long articleNo, String articleSubject, String content, CommentStatus status, LocalDateTime writeDate) {
        this.commentNo = commentNo;
        this.articleNo = articleNo;
        this.articleSubject = articleSubject;
        this.content = content;
        this.commentStatus = status;
        this.writeDate = writeDate;
    }

}
