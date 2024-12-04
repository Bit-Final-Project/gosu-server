package com.ncp.moeego.reserve.bean;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.ncp.moeego.member.entity.Member;

@Entity
@Table(name = "reserve_time")
@Data
public class ReserveTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_time_no")
	private Long reserveTimeNo;
	
	// 하나의 예약은 여러개의 시간을 선택할수있다.
    @ManyToOne
    @JoinColumn(nullable = false)
    private Reserve reserve;
    // 한명의 사용자는 여러개의 예약을 할수있고, 여러개의 예약 시간을 선택할수있다.
    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member memberNo;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
