package com.ncp.moeego.member.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class CancelDTO {
    
	private String name;
	private String phone;
	private String email;
    private Long cancelNo;
    private LocalDateTime cancelDate;
    private String reason;

    // 생성자
    public CancelDTO(String name, String phone, String email, Long cancelNo, LocalDateTime cancelDate, String reason) {
    	this.name = name;
    	this.phone = phone;
    	this.email = email;
        this.cancelNo = cancelNo;
        this.cancelDate = cancelDate;
        this.reason = reason;
    }
    
}

