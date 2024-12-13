package com.ncp.moeego.member.service.impl;

import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.cancel.repository.CancelRepository;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final CancelRepository cancelRepository;
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

    @Override
    public boolean checkMember(String email, String pwd) {
        if(email.equals("")) return false;
        if(pwd.equals("")) return false;

        String memberPwd = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")).getPwd();
        return bCryptPasswordEncoder.matches(pwd, memberPwd);
    }

    @Override
    @Transactional
    public boolean cancelMember(SignOutDTO signOutDTO) {
        boolean check = false;
        try {
            // 이메일로 회원 찾기
            Member member = memberRepository.findByEmail(signOutDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

            // Cancel 객체 생성
            Cancel cancel = new Cancel();
            cancel.setMemberNo(member); // 해당 회원 정보 설정
            cancel.setReason(signOutDTO.getReason()); // 사유 설정

            member.setMemberStatus(MemberStatus.ROLE_CANCEL);

            // Cancel 엔티티 저장
            cancelRepository.save(cancel);
            memberRepository.save(member);
            check = true;
        } catch (UsernameNotFoundException e) {
            // 회원을 찾을 수 없을 때 처리
            log.error("회원 정보 오류: {}", e.getMessage());
        } catch (Exception e) {
            // 일반적인 예외 처리
            log.error("회원 탈퇴 처리 중 오류 발생: {}", e.getMessage());
        }
        return check; // 성공 여부 반환
    }

}
