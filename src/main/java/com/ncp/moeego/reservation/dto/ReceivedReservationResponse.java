package com.ncp.moeego.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class ReceivedReservationResponse {
    private String memberName; // 고객명
    private String proItemName;
    private LocalDate startDate;
    private List<LocalTime> startTimes;
}
