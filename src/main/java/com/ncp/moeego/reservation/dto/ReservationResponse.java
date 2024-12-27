package com.ncp.moeego.reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {
    private Long proNo;
    private String proName;
    private Long proItemNo;
    private String proItemName;
    private Long memberNo;
    private String memberName;
    private Long reservationNo;

    private LocalDate startDate;
    private List<LocalTime> startTimes;

}
