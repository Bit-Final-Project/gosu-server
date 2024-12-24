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
    private Long reservationNo;

    private LocalDate startDate;
    private List<LocalTime> startTimes;

}
