package com.ncp.moeego.member.service.impl;

<<<<<<< Updated upstream
import com.ncp.moeego.member.bean.JwtDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
=======
import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.member.bean.LoginDTO;
import com.ncp.moeego.member.bean.MemberDetails;
>>>>>>> Stashed changes
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.bean.JoinDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    public boolean isExist(String email){
        return memberRepository.existsByEmail(email);
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

    public Long getMemberNo(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")).getMemberNo();

    }

    @Transactional
    public void setMemberStatus(Long memberNo, MemberStatus memberStatus) {
        Member member = memberRepository.findById(memberNo).orElseThrow(()-> new IllegalArgumentException("Invalid memberNo"));
        member.setMemberStatus(memberStatus);
        log.debug("MemberNo: {}, MemberStatus: {}", member.getMemberNo(), member.getMemberStatus());
    }

<<<<<<< Updated upstream
=======
    @Override
    public boolean checkMember(String email, String pwd) {
        if(email.equals("")) return false;
        if(pwd.equals("")) return false;

        String memberPwd = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")).getPwd();
        return bCryptPasswordEncoder.matches(pwd, memberPwd);
    }

    @Override
    public boolean cancelMember(Cancel cancel) {

        return false;
    }

>>>>>>> Stashed changes
}
