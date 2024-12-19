package com.ncp.moeego.pro.entity;

import com.ncp.moeego.category.entity.MainCategory;
import com.ncp.moeego.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pro")
@Data
public class Pro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_no")
    private Long proNo;

    @ManyToOne // 고수는 하나의 메인 카테고리를 선택할수있음
    @JoinColumn(name = "main_cate_no", nullable = false)
    private MainCategory mainCategory;

    @Column(name = "access_date")
    private LocalDateTime accessDate;

    @Column(name = "star")
    private float star;

    @Column(name = "review_count")
    private Long reviewCount;

    @OneToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(name = "one_intro", length = 1000)
    private String oneIntro; // 한줄소개

    @Column(length = 3000)
    private String intro; // 서비스 소개

    // ProItem 와 연관 설정
    @OneToMany(mappedBy = "pro", cascade = CascadeType.ALL)
    private List<ProItem> proItems = new ArrayList<>();

}
