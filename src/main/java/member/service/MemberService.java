package member.service;

import member.bean.JoinDTO;
import member.entity.Member;

public interface MemberService {
    boolean write(JoinDTO joinDTO);
    Member getMemberById(Long memberNo);
    String getMemberName(Long memberNo);
    String getMemberProfileImage(Long memberNo);
}
