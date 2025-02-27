package com.ncp.moeego.member.service.impl;

import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.cancel.repository.CancelRepository;
import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
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
    public Member getMemberByEmail(String email) {
    	return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + email));
    }

    @Override
    public String getMemberEmail(Long memberNo) {
        return getMemberById(memberNo).getEmail();
    }
    
    @Override
    public Integer getMemberEmailStatus(Long memberNo) {
        return getMemberById(memberNo).getEmailStatus();
    }
    
    @Override
    public String getMemberName(Long memberNo) {
        return getMemberById(memberNo).getName();
    }

    @Override
    public String getMemberProfileImage(Long memberNo) {
        return getMemberById(memberNo).getProfileImage();
    }

    @Override
    public Long getMemberNo(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")).getMemberNo();
    }

    @Override
    @Transactional
    public void setMemberStatus(Long memberNo, MemberStatus memberStatus) {
        Member member = memberRepository.findById(memberNo).orElseThrow(()-> new IllegalArgumentException("Invalid memberNo"));
        member.setMemberStatus(memberStatus);
        log.debug("MemberNo: {}, MemberStatus: {}", member.getMemberNo(), member.getMemberStatus());
    }

    @Override
    public String updateName(String email, String name) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        member.setName(name);
        memberRepository.save(member);
        return name;
    }

    @Override
    @Transactional
    public ApiResponse updatePwd(String email, String pwd) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            member.setPwd(bCryptPasswordEncoder.encode(pwd));
            memberRepository.save(member);
            return ApiResponse.success("수정이 완료되었습니다.", null);
        } catch (Exception e) {
            return ApiResponse.error("비밀번호 수정 처리 중 오류가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    @Override
    @Transactional
    public ApiResponse updateAddress(String email, String address) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            member.setAddress(address);
            memberRepository.save(member);
            return ApiResponse.success("수정이 완료되었습니다.", null);
        } catch (Exception e) {
            return ApiResponse.error("수정 처리 중 오류가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    @Override
    @Transactional
    public ApiResponse updatePhone(String email, String phone) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            if(!member.getPhone().equals(phone)) {
                member.setPhone(phone);
                memberRepository.save(member);
                return ApiResponse.success("수정이 완료되었습니다.", phone);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            return ApiResponse.error("수정 처리 중 오류가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    @Override
    public ApiResponse getProCountList() {
        return ApiResponse.success("조회 완료", memberRepository.findByProCountList());
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
    public ApiResponse cancelMember(SignOutDTO signOutDTO) {
        try {
            // 이메일로 회원 조회
            Member member = memberRepository.findByEmail(signOutDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

            // 이미 탈퇴 처리된 회원인지 확인
            if (member.getMemberStatus() == MemberStatus.ROLE_CANCEL) {
                throw new IllegalStateException("이미 탈퇴 처리가 완료된 사용자입니다.");
            }

            // Cancel 엔티티 생성 및 저장
            Cancel cancel = new Cancel();
            cancel.setMember(member);
            cancel.setReason(signOutDTO.getReason());
            cancelRepository.save(cancel);

            // 회원 상태 업데이트
            member.setMemberStatus(MemberStatus.ROLE_CANCEL);
            memberRepository.save(member);

            return ApiResponse.success("회원 탈퇴가 완료되었습니다.", null);

        } catch (UsernameNotFoundException e) {
            return ApiResponse.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.name());
        } catch (IllegalStateException e) {
            return ApiResponse.error("이미 탈퇴 처리가 완료된 사용자입니다.", HttpStatus.CONFLICT.name());
        } catch (Exception e) {
            return ApiResponse.error("회원 탈퇴 처리 중 오류가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

	@Override
	public boolean updateEmailStatus(String email, int currentStatus) {
		Member member = memberRepository.findByEmail(email).orElse(null);
	    if (member != null) {
	        member.setEmailStatus(currentStatus);
	        memberRepository.save(member); // 업데이트 처리
	        return true;
	    }
	    return false;
	}

	@Override
	public Integer getEmailStatusByName(String username) {
		return memberRepository.findEmailStatusByName(username);
	}
	
	

	

}
