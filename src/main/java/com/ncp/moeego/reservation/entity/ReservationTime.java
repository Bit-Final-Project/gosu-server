package com.ncp.moeego.reservation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "reservation_time")
public class ReservationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_time_no")
    private Long reservationTimeNo;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "reservation_no", nullable = false)
    private Reservation reservation;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // 예약한 날짜

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // 예약한 날짜의 시작 시간

    public LocalTime getEndTime() {
        return startTime.plusHours(1);
    }

}
