package com.ncp.moeego.cancel.entity;

import com.ncp.moeego.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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
