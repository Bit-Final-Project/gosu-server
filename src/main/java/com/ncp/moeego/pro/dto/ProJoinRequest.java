package com.ncp.moeego.pro.dto;

import lombok.Data;

@Data
public class ProJoinRequest {

    private Long mainCateNo;
    private String subCategories;
    private String oneIntro;
    private String intro;

    private String email;
    private String name;
    private String pwd;
    private Integer gender;
    private String phone;
    private String address;

}
