package com.ncp.moeego.reservation.service;

import com.ncp.moeego.reservation.dto.ReservationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface ReservationService {
    void makeReservation(ReservationRequest reservationRequest);
}
