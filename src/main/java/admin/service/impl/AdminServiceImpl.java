package admin.service.impl;

import admin.repository.AdminMemberRepository;
import admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import member.bean.MemberStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMemberRepository adminMemberRepository;

    public AdminServiceImpl(AdminMemberRepository adminMemberRepository) {
        this.adminMemberRepository = adminMemberRepository;
    }

    @Override
    public String adminLogin(String email, String pwd) {
        log.debug("email:{},pwd:{} AdminServiceImpl 도착", email, pwd);
        return adminMemberRepository.adminLogin(email, pwd);
    }

    @Override
    public long getUserCount(MemberStatus memberStatus) {
        return adminMemberRepository.countByMemberStatus(memberStatus);

    }
}
