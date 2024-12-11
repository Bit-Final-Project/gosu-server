package com.ncp.moeego.member.bean.oauth2;

import java.util.Map;

public class NaverResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getGender() {
        Object gender = attribute.get("gender");
        return gender != null ? gender.toString() : null;
    }

    @Override
    public String getProfileImage() {
        Object profileImage = attribute.get("profile_image");
        return profileImage != null ? profileImage.toString() : null;
    }

    @Override
    public String getPhone() {
        Object phone = attribute.get("mobile");
        return phone != null ? phone.toString() : null;
    }
}
