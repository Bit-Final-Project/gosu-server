package com.ncp.moeego.reservation.controller;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.reservation.dto.ReservationRequest;
import com.ncp.moeego.reservation.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @DeleteMapping
    public ResponseEntity<?> deleteReservation(Authentication authentication, @RequestParam("reservationNo") Long reservationNo) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        log.info("예약취소 요청 멤버 이름: {}, 예약 번호: {}", memberDetails.getName(), reservationNo);
        String message = reservationService.deleteReservation(authentication.getName(), reservationNo);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    // 예약 총 수 조회
    @GetMapping("/total")
    public ResponseEntity<?> getReservationCount(@RequestParam("proItemNo") Long proItemNo) {
        Long count = reservationService.getReservationCountByProItem(proItemNo);

        if (count == 0) {
            return ResponseEntity.ok(ApiResponse.success("예약된 정보가 없습니다.", null));
        }
        return ResponseEntity.ok(ApiResponse.success("예약 수 조회 성공", count));
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getReservations(Authentication authentication, @RequestParam(required = false) Integer year) {
        log.info("요청한 연도 : {}", year);
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        log.info("조회할 연도 : {}", year);

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        log.info("로그인 정보 : {}", memberDetails.getName());
        log.info("로그인 이메일 : {}", authentication.getName());

        return ResponseEntity.ok(ApiResponse.success("예약내역 조회 성공", reservationService.getReservations(authentication.getName(), year)));
    }

}
