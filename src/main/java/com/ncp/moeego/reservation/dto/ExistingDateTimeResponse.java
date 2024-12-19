package com.ncp.moeego.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ExistingDateTimeResponse {
    private LocalDate startDate;
    private LocalTime startTime;
}
