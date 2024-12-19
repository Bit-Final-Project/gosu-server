package com.ncp.moeego.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class ReservationRequest {

    Long memberNo;
    private Long proItemNo;
    private LocalDate startDate;
    private List<LocalTime> startTimes;
}
