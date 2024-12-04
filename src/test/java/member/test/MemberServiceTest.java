package member.test;

import com.main.moeego.MoeegoApplication;
import member.bean.Member;
import member.bean.MemberStatus;
import member.service.MemberService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MoeegoApplication.class)
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    @Disabled
    void findIdTest() {
        String check = memberService.isExistEmail("admin@naver.com");
        System.out.println(check);
    }

    @Test
    void signUpTest() {
        Member member = new Member();
        member.setEmail("test");
        member.setPwd("test");
        member.setName("테스트");
        member.setMemberStatus(MemberStatus.USER);
        member.setAddress("test");
        member.setPhone("test");
        member.setGender(1);
        boolean check = memberService.write(member);
        System.out.println(check);
    }

    @Test
    void signOutTest() {
        Member member = new Member();
        member.setEmail("test");
        member.setPwd("test");
        boolean check = memberService.delete(member);
        System.out.println(check);
    }

    @Test
    void login() {
        Member member = new Member();
        member.setEmail("test");
        member.setPwd("test");
        boolean check = memberService.login("test", "test");
        System.out.println(check);
    }
}
