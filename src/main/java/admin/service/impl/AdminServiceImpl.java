package admin.service.impl;

import admin.repository.AdminMemberRepository;
import admin.service.AdminService;
import member.bean.MemberStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMemberRepository adminMemberRepository;

    public AdminServiceImpl(AdminMemberRepository adminMemberRepository) {
        this.adminMemberRepository = adminMemberRepository;
    }

    @Override
    public String adminLogin(String email, String pwd) {
        System.out.println(email);
        System.out.println(pwd);
        return adminMemberRepository.adminLogin(email, pwd);
    }

    @Override
    public long getUserCount(MemberStatus memberStatus) {
        return adminMemberRepository.countByMemberStatus(memberStatus);

    }
}
