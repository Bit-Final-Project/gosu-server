package member.service;

import lombok.RequiredArgsConstructor;
import member.bean.MemberDetails;
import member.entity.Member;
import member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername email : " + email);
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            return new MemberDetails(member);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
