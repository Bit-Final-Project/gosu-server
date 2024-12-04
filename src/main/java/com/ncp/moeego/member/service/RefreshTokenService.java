package com.ncp.moeego.member.service;

import com.ncp.moeego.member.entity.Refresh;
import com.ncp.moeego.member.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshRepository refreshRepository;
    @Transactional
    public void saveRefresh(String email, String name, Integer expireS, String refresh) {
        Refresh refreshEntity = Refresh.builder()
                .email(email)
                .name(name)
                .refresh(refresh)
                .expiration(new Date(System.currentTimeMillis() + expireS * 1000L).toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
}
