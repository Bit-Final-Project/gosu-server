package com.ncp.moeego.reservation.service;

import com.ncp.moeego.reservation.dto.ReservationRequest;

public interface ReservationService {
    String makeReservation(ReservationRequest reservationRequest);
}
