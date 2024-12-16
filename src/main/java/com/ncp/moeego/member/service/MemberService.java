package com.ncp.moeego.member.service;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.entity.Member;

public interface MemberService {
    boolean write(JoinDTO joinDTO);
    Member getMemberById(Long memberNo);
    String getMemberName(Long memberNo);
    String getMemberProfileImage(Long memberNo);
    boolean isExist(String email);
    boolean checkMember(String email, String pwd);
    ApiResponse cancelMember(SignOutDTO signOutDTO);
    String updateName(String email, String name);
}
