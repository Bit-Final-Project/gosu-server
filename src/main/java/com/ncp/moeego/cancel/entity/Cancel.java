package com.ncp.moeego.cancel.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import com.ncp.moeego.member.entity.Member;

@Entity
@Data
public class Cancel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cancel_no")
	private Long cancelNo;
	
	@OneToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;
	
	@Column(length = 3000)
	private String reason;

	@Column(name = "cancel_date")
	private LocalDateTime cancelDate = LocalDateTime.now();
	
	
}
