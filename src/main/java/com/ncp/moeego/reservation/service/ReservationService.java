package com.ncp.moeego.reservation.service;

import com.ncp.moeego.reservation.dto.ExistingDateTimeResponse;
import com.ncp.moeego.reservation.dto.ReservationRequest;

import java.util.List;

public interface ReservationService {
    String makeReservation(ReservationRequest reservationRequest);

    List<ExistingDateTimeResponse> getReservationByProItem(Long proItemNo);
}
