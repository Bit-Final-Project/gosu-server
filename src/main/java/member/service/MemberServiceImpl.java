package member.service;

import lombok.RequiredArgsConstructor;
import member.bean.JoinDTO;
import member.entity.Member;
import member.bean.MemberDetails;
import member.entity.MemberStatus;
import member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public boolean write(JoinDTO joinDTO) {
        String email = joinDTO.getEmail();
        String pwd = joinDTO.getPwd();

        Boolean isExist = memberRepository.existsByEmail(email);
        if(isExist) return false;

        Member data = new Member();

        data.setEmail(email);
        data.setName(joinDTO.getName());
        data.setPwd(bCryptPasswordEncoder.encode(pwd));
        data.setAddress(joinDTO.getAddress());
        data.setPhone(joinDTO.getPhone());
        data.setGender(joinDTO.getGender());
        data.setMemberStatus(MemberStatus.ROLE_USER);

        memberRepository.save(data);
        return true;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberData = memberRepository.findByEmail(username);

        if (memberData != null) {
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new MemberDetails(memberData);
        }

        return null;
    }


}
