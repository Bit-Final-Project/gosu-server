package member.service;

import lombok.RequiredArgsConstructor;
import member.bean.Member;
import member.dao.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public String isExistEmail(String email) {
        String getEmail = memberRepository.findByEmailLike(email);
        return getEmail != null ? "exist" : "non_exist";
    }
}
