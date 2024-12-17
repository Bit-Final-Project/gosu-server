package com.ncp.moeego.member.bean;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProDTO {
    private Long memberNo;
    private String name;
    private LocalDateTime accessDate;
    private float star;
    private LocalDateTime depriveDate;
    private Long proNo;
    private String mainCateName;
    private String oneIntro; // 한줄소개 추가
    private String intro;    // 서비스 소개 추가

    // 생성자 업데이트
    public ProDTO(Long memberNo, String name, LocalDateTime accessDate, float star, 
                  LocalDateTime depriveDate, Long proNo, String mainCateName, 
                  String oneIntro, String intro) {
        this.memberNo = memberNo;
        this.name = name;
        this.accessDate = accessDate;
        this.star = star;
        this.depriveDate = depriveDate;
        this.proNo = proNo;
        this.mainCateName = mainCateName;
        this.oneIntro = oneIntro;
        this.intro = intro;
    }
}
