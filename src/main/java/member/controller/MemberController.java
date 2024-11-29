package member.controller;

import lombok.RequiredArgsConstructor;
import member.bean.JoinDTO;
import member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public ResponseEntity mainP() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/join")
    public ResponseEntity joinProcess(@RequestBody JoinDTO joinDTO) {

        System.out.println(joinDTO.getEmail());
        boolean check = memberService.write(joinDTO);
        if(check) return ResponseEntity.ok("ok");
        else return  ResponseEntity.badRequest().body("값이 잘못 되었습니다");
    }
}
