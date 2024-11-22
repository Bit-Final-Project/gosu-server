package member.test;

import com.main.moeego.MoeegoApplication;
import member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MoeegoApplication.class)
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    void findIdTest() {
        String check = memberService.isExistEmail("admin@naver.com");
        System.out.println(check);
    }
}
