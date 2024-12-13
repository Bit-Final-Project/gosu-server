package com.ncp.moeego.member.service.impl;

import com.ncp.moeego.member.bean.JwtDTO;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.MemberInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoProviderImpl implements MemberInfoProvider {
    private final MemberRepository memberRepository;

    @Override
    public JwtDTO getJwtDtoByEmail(String email) {
        return new JwtDTO(memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")));
    }
}
