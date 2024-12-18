package com.ncp.moeego.member.bean;

import java.time.LocalDateTime;

import com.ncp.moeego.member.entity.MemberStatus;

import lombok.Data;

@Data
public class ProDTO {
    private Long memberNo;
    private String name;
    private LocalDateTime accessDate;
    private float star;
    private Long proNo;
    private String mainCateName;
    private String oneIntro; // 한줄소개 추가
    private String intro;    // 서비스 소개 추가
    private MemberStatus memberStatus;

    // 생성자 업데이트 (status 제거)
    public ProDTO(Long memberNo, String name, LocalDateTime accessDate, float star, 
                  Long proNo, String mainCateName, 
                  String oneIntro, String intro, MemberStatus memberStatus) {
        this.memberNo = memberNo;
        this.name = name;
        this.accessDate = accessDate;
        this.star = star;
        this.proNo = proNo;
        this.mainCateName = mainCateName;
        this.oneIntro = oneIntro;
        this.intro = intro;
        this.memberStatus = memberStatus;
    }
}
