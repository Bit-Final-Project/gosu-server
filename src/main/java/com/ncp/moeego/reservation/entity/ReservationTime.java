package com.ncp.moeego.reservation.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name =  "reservation_time")
public class ReservationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_time_no")
    private Long reservationTimeNo;

    @ManyToOne
    @JoinColumn(name = "reservation_no", nullable = false)
    private Reservation reservation;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    public LocalDateTime getEndTime() {
        return startTime.plusHours(1);
    }

}
