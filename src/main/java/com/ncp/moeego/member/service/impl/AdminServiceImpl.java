package com.ncp.moeego.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

    @Override
    public int getRoleUserCount() {
        return memberRepository.countByMemberStatus(MemberStatus.ROLE_USER);
    }

    @Override
    public int getRoleCancelCount() {
        return memberRepository.countByMemberStatus(MemberStatus.ROLE_CANCEL);
    }

	@Override
	public int getRoleProCount() {
		return memberRepository.countByMemberStatus(MemberStatus.ROLE_PRO);
	}

	@Override
	public List<MemberSummaryDTO> getPendingProMembers() {
        return memberRepository.findMemberSummaryByStatus(MemberStatus.ROLE_PEND_PRO);
    }

	
	
	
	
}
