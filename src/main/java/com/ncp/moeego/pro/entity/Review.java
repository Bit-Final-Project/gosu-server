package com.ncp.moeego.pro.entity;

import com.ncp.moeego.category.bean.MainCategory;
import com.ncp.moeego.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo;

    @ManyToOne // 한 고수게시판에는 여러개의 리뷰가 달릴수있음
    @JoinColumn(name = "pro_article_no", nullable = false)
    private ProServiceItem proServiceItem;

    @OneToOne // 해당 고수의 카테고리 전용 게시판이 존재함
    @JoinColumn(name = "main_cate_no", nullable = false)
    private MainCategory mainCateNo;

    @ManyToOne // 고수 게시판에 사용자는 여러개의 리뷰를 작성할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member memberNo;

    private float star;

    @Column(name = "write_date")
    private LocalDateTime writeDate = LocalDateTime.now();
}

