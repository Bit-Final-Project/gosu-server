package member.service;

import member.bean.Member;

public interface MemberService {
    String isExistEmail(String email);

    boolean write(Member member);

    boolean delete(Member member);

    boolean login(String email, String pwd);

    Member getMemberById(Long memberNo);

    String getMemberName(Long memberNo);

    String getMemberProfileImage(Long memberNo);
}
