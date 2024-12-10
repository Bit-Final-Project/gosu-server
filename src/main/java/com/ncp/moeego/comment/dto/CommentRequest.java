package com.ncp.moeego.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequest {

    private Long commentNo;
    private Long memberNo;
    private Long articleNo;
    private String content;
    private Long parentCommentNo; //없으면 0
/*
    public Long getParentCommentNo() {
        return (parentCommentNo == null) ? 0L : parentCommentNo;
    }*/

}
