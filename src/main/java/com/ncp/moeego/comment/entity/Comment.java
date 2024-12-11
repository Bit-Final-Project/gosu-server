package com.ncp.moeego.comment.entity;

import com.ncp.moeego.article.bean.Article;
import com.ncp.moeego.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(
        name = "comment",
        indexes = {
                @Index(name = "idx_article_write_date", columnList = "article_no, write_date")
        }
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Long commentNo; // 댓글 ID

    @ManyToOne
    @JoinColumn(name = "article_no", nullable = false)
    private Article article; // 게시글 참조

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member; // 작성자 참조

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent; // 부모 댓글 참조

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> children = new ArrayList<>(); // 대댓글 리스트

    @Column(length = 5000, nullable = false)
    private String content; // 댓글 내용

    @Column(name = "comment_status")
    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus; // 댓글 상태

    @Column(name = "write_date", nullable = false)
    private LocalDateTime writeDate; //댓글 작성 시간

    @PrePersist
    public void prePersist() {
        this.writeDate = LocalDateTime.now();
        if (this.commentStatus == null) {
            this.commentStatus = CommentStatus.DEFAULT;
        }
    } // 자동으로 입력

}
