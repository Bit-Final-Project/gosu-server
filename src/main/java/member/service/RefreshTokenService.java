package member.service;

import lombok.RequiredArgsConstructor;
import member.entity.Refresh;
import member.repository.RefreshRepository;
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
