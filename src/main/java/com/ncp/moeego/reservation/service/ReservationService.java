package com.ncp.moeego.reservation.service;

import com.ncp.moeego.reservation.dto.ReservationRequest;
import com.ncp.moeego.reservation.dto.ReservationResponse;

import java.util.List;
import java.util.Map;

public interface ReservationService {
    String makeReservation(ReservationRequest reservationRequest);

    Map<String, Object> getReservations(String email, Integer year);
  
    Long getReservationCountByProItem(Long proItemNo); // 예약 수 조회 메서드

    String deleteReservation(String email, Long reservationNo);

    List<ReservationResponse> getReservationByPro(Long proNo);
}
