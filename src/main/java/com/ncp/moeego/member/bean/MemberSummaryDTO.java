package com.ncp.moeego.member.bean;

import lombok.Data;

@Data
public class MemberSummaryDTO {
	private Long member_no;
    private String name;
    private String oneIntro;
    private Long depriveDateCount;

    public MemberSummaryDTO(Long member_no, String name, String oneIntro, Long depriveDateCount) {
        this.member_no = member_no;
        this.name = name;
        this.oneIntro = oneIntro;
        this.depriveDateCount = depriveDateCount;
    }
}
