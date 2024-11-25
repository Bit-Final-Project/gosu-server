package admin.controller;

import admin.dto.LoginRequest;
import admin.service.AdminService;
import member.bean.MemberStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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
        long masterCount = adminService.getUserCount(MemberStatus.PRO);
        long deactivatedCount = adminService.getUserCount(MemberStatus.CANCEL);

    System.out.println(userCount);
    System.out.println(masterCount);
    System.out.println(deactivatedCount);

    return "DASHBOARD";


    }


    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(RequestEntity<LoginRequest> requestEntity) {

        LoginRequest loginRequest = requestEntity.getBody();
        String contentType = requestEntity.getHeaders().getContentType().toString();
        String url = requestEntity.getUrl().toString();

        System.out.println(contentType);
        System.out.println(url);
        System.out.println(requestEntity.getBody().toString());

        assert loginRequest != null;
        String adminName = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPwd());

        if (adminName != null) {
            return ResponseEntity.ok("인증성공");
        } else {
            return ResponseEntity.status(401).body("인증실패");
        }


    }

}
