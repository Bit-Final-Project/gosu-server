package member.service;

import lombok.RequiredArgsConstructor;
import member.bean.Member;
import member.dao.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public String isExistEmail(String email) {
        String getEmail = memberRepository.findByEmailLike(email);
        return getEmail != null ? "exist" : "non_exist";
    }

    @Override
    @Transactional
    public boolean write(Member member) {
        Member savedMember = memberRepository.save(member);
        return savedMember != null && savedMember.getEmail() != null ? true : false;
    }

    @Override
    public boolean login(String email, String pwd) {
        Member member = memberRepository.login(email, pwd);
        return member != null && member.getEmail() != null ? true : false;
    }

    @Override
    @Transactional
    public boolean delete(Member member) {
        Member getMember = memberRepository.login(member.getEmail(), member.getPwd());
        if (getMember != null && getMember.getEmail() != null) {
            memberRepository.delete(member);
            return true;
        } else return false;
    }

}
