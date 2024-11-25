package member.service;

import member.bean.Member;

public interface MemberService {
    String isExistEmail(String email);
    boolean write(Member member);
    boolean delete(Member member);
    boolean login(String email, String pwd);
}
