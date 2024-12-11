package com.ncp.moeego.comment.repository;

import com.ncp.moeego.comment.entity.Comment;
import com.ncp.moeego.comment.dto.MemberCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            SELECT new com.ncp.moeego.comment.dto.MemberCommentResponse (
                c.commentNo,
                c.article.articleNo,
                c.article.subject,
                c.content,
                c.commentStatus,
                c.writeDate)
            FROM Comment c
            WHERE c.member.memberNo = :memberNo
            ORDER BY c.writeDate ASC
            """)
    Page<MemberCommentResponse> findByMember_MemberNo(@Param("memberNo") Long memberNo, Pageable pageable);

    List<Comment> findByArticle_ArticleNo(Long articleNo);

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.article.articleNo = :articleNo AND c.parent IS NULL
            ORDER BY c.writeDate ASC
            """)
    Page<Comment> findParentCommentsByArticle(@Param("articleNo") Long articleNo, Pageable pageable);
    
    // 상세조회 댓글 수 조회 쿼리
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.article.articleNo = :articleNo")
    public int countByArticleNo(@Param("articleNo") Long articleNo);

}
