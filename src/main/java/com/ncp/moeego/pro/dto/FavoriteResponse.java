package com.ncp.moeego.pro.dto;

import lombok.Data;

@Data
public class FavoriteResponse {

    private Long proNo;
    private String name;
    private String profileImage;
    private Long mainCateNo;
    private float star;
    private String oneIntro;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long proNo, String name, String profileImage, Long mainCateNo, float star, String oneIntro) {
        this.proNo = proNo;
        this.name = name;
        this.profileImage = profileImage;
        this.mainCateNo = mainCateNo;
        this.star = star;
        this.oneIntro = oneIntro;
    }
}
