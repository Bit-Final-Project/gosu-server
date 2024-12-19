package com.ncp.moeego.image.entity;

import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.pro.entity.ProItem;
import com.ncp.moeego.review.entity.Review;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_no")
    private Long imageNo;

    @ManyToOne // 여러개의 이미지를 한 리뷰에 넣을수있음
    @JoinColumn(name = "review_no")
    private Review review;

    @ManyToOne // 여러개의 이미지를 고수 게시판에 넣을수있음
    @JoinColumn(name = "pro_item_no", nullable = true)
    private ProItem proItem;

    @ManyToOne // 여러개의 이미지를 게시판에 넣을수있음
    @JoinColumn(name = "article_no", nullable = true)
    private Article article;

    @ManyToOne // 여러개의 이미지를 사용자가 넣을수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(length = 3000)
    private String imageName;

    @Column(length = 3000)
    private String imageUuidName;

}
