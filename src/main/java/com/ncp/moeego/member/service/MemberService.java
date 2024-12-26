package com.ncp.moeego.member.service;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;

public interface MemberService {
    boolean write(JoinDTO joinDTO);

    Member getMemberById(Long memberNo);

    Member getMemberByEmail(String email);

    String getMemberEmail(Long memberNo);
    
    String getMemberName(Long memberNo);

    String getMemberProfileImage(Long memberNo);

    boolean isExist(String email);

    boolean checkMember(String email, String pwd);

    ApiResponse cancelMember(SignOutDTO signOutDTO);

    String updateName(String email, String name);

    ApiResponse updatePwd(String email, String pwd);

    ApiResponse updateAddress(String email, String address);

    ApiResponse updatePhone(String email, String phone);

    Long getMemberNo(String email);

    void setMemberStatus(Long memberNo, MemberStatus memberStatus);


}
