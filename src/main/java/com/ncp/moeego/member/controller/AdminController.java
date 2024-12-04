package com.ncp.moeego.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @PostMapping("/admin")
    public ResponseEntity adminP() {
        return ResponseEntity.ok("admin");
    }
}
