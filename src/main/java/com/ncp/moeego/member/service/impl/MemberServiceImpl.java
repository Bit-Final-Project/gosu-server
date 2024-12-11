package com.ncp.moeego.member.service.impl;

import com.ncp.moeego.member.bean.JwtDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.bean.JoinDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public boolean write(JoinDTO joinDTO) {
        String email = joinDTO.getEmail();
        String pwd = joinDTO.getPwd();

        Boolean isExist = memberRepository.existsByEmail(email);
        if(isExist) return false;

        Member data = new Member();

        data.setEmail(email);
        data.setName(joinDTO.getName());
        data.setPwd(bCryptPasswordEncoder.encode(pwd));
        data.setGender(joinDTO.getGender());
        data.setAddress(joinDTO.getAddress());
        data.setPhone(joinDTO.getPhone());
        data.setGender(joinDTO.getGender());
        data.setMemberStatus(MemberStatus.ROLE_USER);

        memberRepository.save(data);
        return true;
    }

    @Override
    public Member getMemberById(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberNo));
    }

    @Override
    public String getMemberName(Long memberNo) {
        return getMemberById(memberNo).getName();
    }

    @Override
    public String getMemberProfileImage(Long memberNo) {
        return getMemberById(memberNo).getProfileImage();
    }

}
