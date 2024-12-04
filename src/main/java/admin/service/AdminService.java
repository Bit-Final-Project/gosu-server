package admin.service;

import member.bean.MemberStatus;

public interface AdminService {
    String adminLogin(String email, String pwd);

    long getUserCount(MemberStatus memberStatus);
}
