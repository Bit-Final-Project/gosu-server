package admin.service.impl;

import admin.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public boolean adminLogin(String email, String password) {
        System.out.println(email + "서비스까지도착");
        System.out.println(password + "서비스까지도착");
        return true;
    }
}
