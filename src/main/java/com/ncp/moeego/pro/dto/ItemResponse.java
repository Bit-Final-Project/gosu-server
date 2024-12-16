package com.ncp.moeego.pro.dto;

import lombok.Data;

@Data
public class ItemResponse {
    private String name;
    private String profileImage;
    private String address;

    private String mainCategory;
    private String subCategory;

    private String subject;
    private String content;
    private Long price;

    private float star;

    public ItemResponse() {
    }

    public ItemResponse(String name, String profileImage, String address, String mainCategory, String subCategory, String subject, String content, Long price, float star) {
        this.name = name;
        this.profileImage = profileImage;
        this.address = address;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.subject = subject;
        this.content = content;
        this.price = price;
        this.star = star;
    }
}


