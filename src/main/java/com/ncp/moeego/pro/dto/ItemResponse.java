package com.ncp.moeego.pro.dto;

import com.ncp.moeego.pro.entity.ProItem;
import lombok.Data;

import java.util.List;

@Data
public class ItemResponse {

    private Long proNo;
    private String name;
    private String profileImage;
    private String intro;
    private String oneIntro;
    private Long mainCateNo;
    private String mainCateName;
    private float star;
    private Long reviewCount;
    private String address;
    private List<ProItem> proItems;

    public ItemResponse(Long proNo, String name, String profileImage, String intro, String oneIntro, Long mainCateNo, String mainCateName, float star, Long reviewCount, String address, List<ProItem> proItems) {
        this.proNo = proNo;
        this.name = name;
        this.profileImage = profileImage;
        this.intro = intro;
        this.oneIntro = oneIntro;
        this.mainCateNo = mainCateNo;
        this.mainCateName = mainCateName;
        this.star = star;
        this.reviewCount = reviewCount;
        this.address = address;
        this.proItems = proItems;
    }


}
