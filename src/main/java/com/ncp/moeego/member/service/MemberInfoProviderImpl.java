package com.ncp.moeego.member.service;

import com.ncp.moeego.member.bean.JwtDTO;
import com.ncp.moeego.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoProviderImpl implements MemberInfoProvider{
    private final MemberRepository memberRepository;

    @Override
    public JwtDTO getJwtDtoByEmail(String email) {
        return new JwtDTO(memberRepository.findByEmail(email));
    }
}
