package com.ncp.moeego.pro.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProJoinRequest {

    private Long mainCateNo;
    private List<Long> subCategories;
    private String oneIntro;
    private String intro;

    private String email;
    private String name;
    private String pwd;
    private Integer gender;
    private String phone;
    private String address;

}