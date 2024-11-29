package member.service;

import member.bean.MemberEntity;

public interface MemberService {
    String isExistEmail(String email);
    boolean write(MemberEntity member);
    boolean delete(MemberEntity member);
    boolean login(String email, String pwd);
}
