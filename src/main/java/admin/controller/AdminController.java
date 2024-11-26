package admin.controller;

import admin.dto.LoginRequest;
import admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import member.bean.MemberStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

@GetMapping
@ResponseBody
    public String adminDashboard(){
        long userCount = adminService.getUserCount(MemberStatus.USER);
        long proCount = adminService.getUserCount(MemberStatus.PRO);
        long cancelCount = adminService.getUserCount(MemberStatus.CANCEL);

        log.debug("UserCount:{}", userCount);
        log.debug("ProCount:{}", proCount);
        log.debug("CancelCount:{}", cancelCount);

    return "DASHBOARD";


    }

    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(RequestEntity<LoginRequest> requestEntity) {

        LoginRequest loginRequest = requestEntity.getBody();
        String contentType = requestEntity.getHeaders().getContentType().toString();
        String url = requestEntity.getUrl().toString();

        log.debug("Content-Type: {}", contentType);
        log.debug("Request URL: {}", url);
        log.debug("Request Body: {}", loginRequest);

        assert loginRequest != null;
        String adminName = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPwd());

        if (adminName != null) {
            return ResponseEntity.ok("인증성공");
        } else {
            return ResponseEntity.status(401).body("인증실패");
        }


    }

}
