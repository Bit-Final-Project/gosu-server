package com.ncp.moeego.member.bean.oauth2;

import lombok.Builder;
import lombok.Data;
import com.ncp.moeego.member.entity.MemberStatus;

@Data
@Builder
public class MemberDTO {
    private String email;
    private String name;
    private Integer gender;
    private String phone;
    private String address;
    private MemberStatus memberStatus;
}
