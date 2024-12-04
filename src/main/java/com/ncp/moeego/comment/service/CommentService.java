package com.ncp.moeego.comment.service;

import com.ncp.moeego.comment.dto.CommentResponse;
import com.ncp.moeego.comment.dto.CommentRequest;
import com.ncp.moeego.comment.dto.MemberCommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    CommentResponse writeComment(CommentRequest writeRequest);

    CommentResponse updateComment(Long commentNo, String newContent);

    CommentResponse deleteComment(Long commentNo);

    Page<MemberCommentResponse> findCommentsByMember(Long memberNo, int pg, int pageSize);

    List<CommentResponse> findCommentsByArticle(Long articleNo);

    Page<CommentResponse> findPagedCommentsByArticle(Long articleNo, int pg, int pageSize);
}
