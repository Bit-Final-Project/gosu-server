package com.ncp.moeego.member.bean.oauth2;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

	private final Map<String, Object> attribute;
	private final Map<String, Object> kakaoAccount;
	private final Map<String, Object> properties;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.kakaoAccount = (Map<String, Object>) attribute.get("kakaoaccount");
		this.properties = (Map<String, Object>) attribute.get("properties");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return String.valueOf(attribute.get("id"));
	}

	@Override
	public String getEmail() {
		return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
	}

	@Override
	public String getName() {
		return properties != null ? (String) properties.get("nickname") : null;
	}

	@Override
	public String getProfileImage() {
	    if (properties == null) {
	        System.out.println("Properties is null");
	        return null;
	    }
	    String profileImage = (String) properties.get("profile_image");
	    System.out.println("Kakao profile image: " + profileImage);
	    return profileImage;
	}

	@Override
	public String getPhone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGender() {
		// TODO Auto-generated method stub
		return null;
	}

}
