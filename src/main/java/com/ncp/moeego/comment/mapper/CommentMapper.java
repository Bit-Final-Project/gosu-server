package com.ncp.moeego.comment.mapper;

import com.ncp.moeego.article.bean.Article;
import com.ncp.moeego.comment.dto.CommentRequest;
import com.ncp.moeego.comment.dto.CommentResponse;
import com.ncp.moeego.comment.entity.Comment;
import com.ncp.moeego.common.ConvertDate;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.service.MemberService;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final MemberService memberService;

    public CommentMapper(MemberService memberService) {
        this.memberService = memberService;

    }


    public Comment toEntity(CommentRequest request) {

        Comment comment = new Comment();
        comment.setContent(request.getContent());

        Article article = new Article();
        article.setArticleNo(request.getArticleNo());
        comment.setArticle(article);

        Member member = new Member();
        member.setMemberNo(request.getMemberNo());
        comment.setMember(member);

        if (request.getParentCommentNo() != null && request.getParentCommentNo() != 0) {
            Comment parent = new Comment();
            parent.setCommentNo(request.getParentCommentNo());
            comment.setParent(parent);
        }

        return comment;
    }


    public CommentResponse toDTO(Comment comment) {
        CommentResponse response = new CommentResponse();

        response.setCommentNo(comment.getCommentNo());
        response.setArticleNo(comment.getArticle().getArticleNo());
        response.setMemberNo(comment.getMember().getMemberNo());
        response.setContent(comment.getContent());
        response.setCommentStatus(comment.getCommentStatus());
        response.setWriteDate(comment.getWriteDate());
        response.setElapsedTime(ConvertDate.calculateDate(comment.getWriteDate()));
        response.setParentCommentNo(
                comment.getParent() != null ? comment.getParent().getCommentNo() : 0
        );

        //재귀
        for (Comment child : comment.getChildren()) {
            response.getChildren().add(toDTO(child));
        }

        response.setMemberName(memberService.getMemberName(comment.getMember().getMemberNo()));
        response.setMemberProfileImage(memberService.getMemberProfileImage(comment.getMember().getMemberNo()));

        return response;
    }
}
