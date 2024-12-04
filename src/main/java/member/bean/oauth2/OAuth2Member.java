package member.bean.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2Member implements OAuth2User {

    private final MemberDTO memberDTO;

    //소셜로그인마다 차이가 많아 획일화 하기 어려움 -> 구현x
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDTO.getMemberStatus().name();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return memberDTO.getName();
    }

    public String getEmail() {
        return memberDTO.getEmail();
    }

}
