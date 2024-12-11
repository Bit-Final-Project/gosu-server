package com.ncp.moeego.pro.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProApplyRequest {

    private Long memberNo;
    private Long mainCateNo;
    private String subCategories;
    private String oneIntro;
    private String intro;
    private Date depriveDate;

    public ProApplyRequest() {
    }

    public ProApplyRequest(ProJoinRequest request) {
        this.mainCateNo = request.getMainCateNo();
        this.subCategories = request.getSubCategories();
        this.oneIntro = request.getOneIntro();
        this.intro = request.getIntro();
    }

}
