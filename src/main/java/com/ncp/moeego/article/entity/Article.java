package com.ncp.moeego.article.entity;

import com.ncp.moeego.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_no")
    private Long articleNo;

    @ManyToOne(fetch = FetchType.LAZY) // 여러개의 게시판을 한명의 사용자가 작성할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(length = 3000)
    private String subject;

    @Column(length = 5000)
    private String content;

    private int view;

    private int type;
    
    @Column(nullable = false) // NULL 값 허용 안 함
    private int likes = 0; // 기본값 설정

    @Column(name = "write_date")
    private LocalDateTime writeDate = LocalDateTime.now();

    @Column(length = 200)
    private String service;
    
    @Column(length = 200)
    private String area;

    @Override
    public String toString() {
        return "Article{" +
                "articleNo=" + articleNo +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", view=" + view +
                ", type=" + type +
                ", writeDate=" + writeDate +
                ", likes=" + likes +
                ", service" + service +
                ", area" + area +
                '}';
    }

}
