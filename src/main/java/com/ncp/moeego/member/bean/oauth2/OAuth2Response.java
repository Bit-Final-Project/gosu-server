package com.ncp.moeego.member.bean.oauth2;

public interface OAuth2Response {
    //제공자 (Ex. naver, google, ...)
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();
    //사용자 성별
    String getGender();
    //사용자 프로필사진
    String getProfileImage();
    //사용자 핸드폰
    String getPhone();

}