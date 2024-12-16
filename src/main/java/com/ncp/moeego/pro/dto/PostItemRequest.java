package com.ncp.moeego.pro.dto;

import lombok.Data;

@Data
public class PostItemRequest {
    private Long proNo;
    private Long subCateNo;
    private String subject;
    private String content;
    private Long price;
}
