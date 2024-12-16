package com.ncp.moeego.pro.dto;

import lombok.Data;

@Data
public class PostItemRequest {
    Long proNo;
    Long subCateNo;
    String subject;
    String content;
    Long price;
}
