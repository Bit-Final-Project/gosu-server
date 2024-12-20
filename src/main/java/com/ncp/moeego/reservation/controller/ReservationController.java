package com.ncp.moeego.reservation.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.reservation.dto.ReservationRequest;
import com.ncp.moeego.reservation.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<?> getExistingDateTime(@RequestParam("proItemNo") Long proItemNo) {

        return ResponseEntity.ok(ApiResponse.success("예약 조회 성공", reservationService.getReservationByProItem(proItemNo)));

    }

    @PostMapping
    public ResponseEntity<?> makeReservation(@RequestBody ReservationRequest reservationRequest) {
        log.info("예약 요청 memberNo: {}, proItemNo: {}, reservationDate: {}, startTimes: {} ", reservationRequest.getMemberNo(), reservationRequest.getProItemNo(), reservationRequest.getStartDate(), reservationRequest.getStartTimes());
        String message = reservationService.makeReservation(reservationRequest);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

}
