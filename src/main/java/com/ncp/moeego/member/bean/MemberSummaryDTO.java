package com.ncp.moeego.member.bean;

import com.ncp.moeego.member.entity.MemberStatus;

import lombok.Data;

@Data
public class MemberSummaryDTO {
	private Long memberNo;
    private String name;
    private MemberStatus memberStatus;
    private String oneIntro;
    private Long count;

    public MemberSummaryDTO(Long memberNo, String name, MemberStatus memberStatus, String oneIntro, Long count) {
        this.memberNo = memberNo;
        this.name = name;
        this.memberStatus = memberStatus;
        this.oneIntro = oneIntro;
        this.count = count;
    }

}
