package com.ncp.moeego.member.entity;

public enum MemberStatus {
    ROLE_ADMIN(0, "관리자"),
    ROLE_USER(1, "일반 사용자"),
    ROLE_PRO(2, "달인"),
    ROLE_PEND_PRO(3, "달인 미승인"),
    ROLE_CANCEL_PRO(4, "달인 박탈"),
    ROLE_CANCEL(99, "회원 탈퇴");
    private final int code;
    private final String description;

    MemberStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // 코드로 상태를 가져오는 정적 메서드
    public static MemberStatus fromCode(int code) {
        for (MemberStatus status : MemberStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid MemberStatus code: " + code);
    }
}
