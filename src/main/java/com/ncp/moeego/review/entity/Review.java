package com.ncp.moeego.review.entity;

import com.ncp.moeego.image.entity.Image;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.pro.entity.ProItem;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo;

    @ManyToOne // 한 고수게시판에는 여러개의 리뷰가 달릴수있음
    @JoinColumn(name = "pro_item_no", nullable = false)
    private ProItem proItem;

    @ManyToOne // 고수 게시판에 사용자는 여러개의 리뷰를 작성할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(name = "review_content", length = 3000)
    private String reviewContent;

    private float star;

    @Column(name = "write_date")
    private LocalDateTime writeDate = LocalDateTime.now();

}

