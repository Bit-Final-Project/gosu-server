package com.ncp.moeego.reservation.entity;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.pro.entity.ProItem;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name =  "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_no")
    private Long reservationNo;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "pro_item_no", nullable = false)
    private ProItem proItem;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationTime> reservationTimes = new ArrayList<>();


}
