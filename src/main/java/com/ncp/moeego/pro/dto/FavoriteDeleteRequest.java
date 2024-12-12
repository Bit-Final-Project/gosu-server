package com.ncp.moeego.pro.dto;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteDeleteRequest {

    private Long memberNo;
    private List<Long> proNo;

}
