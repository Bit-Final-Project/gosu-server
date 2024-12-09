package com.ncp.moeego.member.bean;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO {
    private Long memberNo;
    private String email;
    private String name;
    private String address;
    private String phone;
    private MemberStatus memberStatus;

    public JwtDTO(Member member) {
        this.memberNo = member.getMemberNo();
        this.email = member.getEmail();
        this.name = member.getName();
        this.address = member.getAddress();
        this.phone = member.getPhone();
        this.memberStatus = member.getMemberStatus();
    }
}