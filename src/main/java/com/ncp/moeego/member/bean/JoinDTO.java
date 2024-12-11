package com.ncp.moeego.member.bean;

import com.ncp.moeego.member.entity.Member;
import lombok.Data;

@Data
public class JoinDTO {
    private String email;
    private String name;
    private String pwd;
    private Integer gender;
    private String phone;
    private String address;

    public JoinDTO(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.gender = member.getGender();
        this.phone = member.getPhone();
        this.address = member.getAddress();
    }
}
