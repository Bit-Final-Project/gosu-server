package com.ncp.moeego.member.service.oauth2;

import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.bean.oauth2.KakaoResponse;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.bean.oauth2.OAuth2Member;
import com.ncp.moeego.member.bean.oauth2.OAuth2Response;
import com.ncp.moeego.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import com.ncp.moeego.member.bean.oauth2.NaverResponse;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if(registrationId.equals("kakao")){
        	oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String oAuth2Id = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        OAuth2Member oAuth2Member = null;

        //DB save
        if (registrationId.equals("naver")) {           
        	saveNaverUser(oAuth2Response, oAuth2Id, MemberStatus.ROLE_USER.name());
        }
        if (registrationId.equals("kakao")) {        	
        	saveKakaoUser(oAuth2Response, oAuth2Id, MemberStatus.ROLE_USER.name());
        }
        
        // Entity 목적 순수하게 유지하기 위해서 dto 로 전달..
        MemberDTO memberDTO = MemberDTO.builder()
                .email(oAuth2Id)
                .name(oAuth2Response.getName())
                .memberStatus(MemberStatus.ROLE_USER)
                .build();

        oAuth2Member = new OAuth2Member(memberDTO);

        // 서버 내부에서 사용하기 위한 인증 정보
        return oAuth2Member;
    }

    @Transactional
    void saveKakaoUser(OAuth2Response oAuth2Response, String oAuth2Id, String memberStatus) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2Id);

        if (optionalMember.isPresent()) {
            Member existData = optionalMember.get();
            String currentProfileImage = existData.getProfileImage();
            String newProfileImage = oAuth2Response.getProfileImage();
            
            existData.setEmail(oAuth2Id);
            existData.setName(oAuth2Response.getName());
            existData.setPhone(oAuth2Response.getPhone() == null ? existData.getPhone() : oAuth2Response.getPhone());
            
            // 새 프로필 이미지가 있을 때만 업데이트, 없으면 기존 이미지 유지
            if (newProfileImage != null && !newProfileImage.isEmpty()) {
                existData.setProfileImage(newProfileImage);
            } else {
                existData.setProfileImage(currentProfileImage);
            }
            
            existData.setGender(oAuth2Response.getGender() == null ? 3 : oAuth2Response.getGender().equals("M") ? 1 : 2);
            memberRepository.save(existData);
        } else {
            Member member = Member.builder()
                    .email(oAuth2Id)
                    .name(oAuth2Response.getName())
                    .pwd("OAuth2-password")
                    .address("OAuth2-address")
                    .phone(oAuth2Response.getPhone() == null ? "OAuth2-phone" : oAuth2Response.getPhone())
                    .gender(oAuth2Response.getGender() == null ? 3 : oAuth2Response.getGender().equals("M") ? 1 : 2)
                    .profileImage(oAuth2Response.getProfileImage())
                    .memberStatus(MemberStatus.valueOf(memberStatus))
                    .joinDate(LocalDateTime.now())
                    .build();
            memberRepository.save(member);
        }
    }

	/**
     * 이미 존재하는 경우 update
     * 존재하지 않는 경우 save
     */
    @Transactional
    void saveNaverUser(OAuth2Response response, String naverId, String memberStatus) {
        // DB 조회
        Optional<Member> optionalMember = memberRepository.findByEmail(naverId);

        if (optionalMember.isPresent()) {
            Member existData = optionalMember.get();
            existData.setEmail(naverId);
            existData.setName(response.getName());
            existData.setPhone(response.getPhone() == null ? existData.getPhone() : response.getPhone());
            existData.setProfileImage(response.getProfileImage() == null ? existData.getProfileImage() : response.getProfileImage());
            existData.setGender(response.getGender() == null ? 3 : response.getGender().equals("M") ? 1 : 2);
            memberRepository.save(existData);
        } else {
            Member member = Member.builder()
                    .email(naverId)
                    .name(response.getName())
                    .pwd("OAuth2-password")
                    .address("OAuth2-address")
                    .phone(response.getPhone() == null ? "OAuth2-phone" : response.getPhone())
                    .gender(response.getGender() == null ? 3 : response.getGender().equals("M") ? 1 : 2)
                    .profileImage(response.getProfileImage())
                    .memberStatus(MemberStatus.valueOf(memberStatus))
                    .joinDate(LocalDateTime.now())
                    .build();
            memberRepository.save(member);
        }
    }

}
