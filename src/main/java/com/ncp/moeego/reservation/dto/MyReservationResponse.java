package com.ncp.moeego.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class MyReservationResponse {
    private Long proNo;
    private String proName;
    private Long proItemNo;
    private String proItemName;

    private LocalDate startDate;
    private List<LocalTime> startTimes;


    /*public MyReservationResponse(Reservation reservation) {
        this.proName = reservation.getProItem().getPro().getMember().getName();
        this.proItemName = reservation.getProItem().getSubject();
        this.startDate = reservation.getReservationTimes().stream().map(reservationTime -> reservationTime.getStartDate()).findFirst().orElse(null);
        this.startTimes = reservation.getReservationTimes().stream().map(reservationTime -> reservationTime.getStartTime()).toList();
    }*/

}
