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
    private String subCategories;
    private Long proNo;
    private String mainCateName;  // mainCateName 추가

    // 생성자에 subCateName 추가
    public ProDTO(Long memberNo, String name, LocalDateTime accessDate, float star, LocalDateTime depriveDate, 
                  String subCategories, Long proNo, String mainCateName) {
        this.memberNo = memberNo;
        this.name = name;
        this.accessDate = accessDate;
        this.star = star;
        this.depriveDate = depriveDate;
        this.subCategories = subCategories;
        this.proNo = proNo;
        this.mainCateName = mainCateName;
    }
}
