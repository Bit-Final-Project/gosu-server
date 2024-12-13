package com.ncp.moeego.member.service;

import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.member.bean.LoginDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.bean.JoinDTO;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface MemberService {
    boolean write(JoinDTO joinDTO);
    Member getMemberById(Long memberNo);
    String getMemberName(Long memberNo);
    String getMemberProfileImage(Long memberNo);
    boolean isExist(String email);
    boolean checkMember(String email, String pwd);
    boolean cancelMember(Cancel cancel);
}
