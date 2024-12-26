package com.ncp.moeego.member.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProCountDTO {
    private Long mainCateNo;
    private String mainCateName;
    private Long count;
}
