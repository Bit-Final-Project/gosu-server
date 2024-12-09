package com.ncp.moeego.member.service;

import com.ncp.moeego.member.bean.JwtDTO;

public interface MemberInfoProvider {
    JwtDTO getJwtDtoByEmail(String email);
}