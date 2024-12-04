package com.ncp.moeego.member.service;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.bean.JoinDTO;

public interface MemberService {
    boolean write(JoinDTO joinDTO);
    Member getMemberById(Long memberNo);
    String getMemberName(Long memberNo);
    String getMemberProfileImage(Long memberNo);
}
