package com.ncp.moeego.member.bean;

import lombok.Data;

@Data
public class SignOutDTO {
    private String email;
    private String pwd;
    private String reason;
}
