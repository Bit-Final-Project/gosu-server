package com.ncp.moeego.favorite.entity;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.pro.entity.Pro;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteNo;

    @ManyToOne // 여러개의 즐겨찾기를 한 사용자가 할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @ManyToOne // 즐겨찾기에 여러명의 고수가 담길수있음
    @JoinColumn(name = "pro_no", nullable = false)
    private Pro pro;

}
