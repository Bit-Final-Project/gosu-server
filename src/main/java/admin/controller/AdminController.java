package admin.controller;

import admin.dto.LoginRequest;
import admin.service.AdminService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("login")
    public ResponseEntity<String> adminLogin(RequestEntity<LoginRequest> requestEntity) {

        LoginRequest loginRequest = requestEntity.getBody();
        String contentType = requestEntity.getHeaders().getContentType().toString();
        String url = requestEntity.getUrl().toString();

        System.out.println(contentType);
        System.out.println(url);
        System.out.println(requestEntity.getBody().toString());

        boolean isAuthenticated = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("인증성공");
        } else {
            return ResponseEntity.status(401).body("인증실패");
        }


    }


}
