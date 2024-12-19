package com.ncp.moeego.reservation.service;

import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.pro.service.ProService;
import com.ncp.moeego.reservation.dto.ExistingDateTimeResponse;
import com.ncp.moeego.reservation.dto.ReservationRequest;
import com.ncp.moeego.reservation.entity.Reservation;
import com.ncp.moeego.reservation.entity.ReservationTime;
import com.ncp.moeego.reservation.repository.ReservationRepository;
import com.ncp.moeego.reservation.repository.ReservationTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final MemberService memberService;
    private final ProService proService;

    public ReservationServiceImpl(ReservationRepository reservationRepository, MemberService memberService, ProService proService, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.proService = proService;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    @Override
    public String makeReservation(ReservationRequest reservationRequest) {

        isExist(reservationRequest);

        Reservation reservation = new Reservation();
        reservation.setMember(memberService.getMemberById(reservationRequest.getMemberNo()));
        reservation.setProItem(proService.getProItemById(reservationRequest.getProItemNo()));
        reservationRepository.save(reservation);

        List<ReservationTime> reservationTimes = reservationRequest.getStartTimes().stream().map(startTime -> {
            ReservationTime reservationTime = new ReservationTime();
            reservationTime.setStartTime(startTime);
            reservationTime.setReservation(reservation);
            reservationTime.setStartDate(reservationRequest.getStartDate());
            return reservationTime;
        }).toList();

        reservationTimeRepository.saveAll(reservationTimes);
        return "예약 성공";
    }

    @Override
    public List<ExistingDateTimeResponse> getReservationByProItem(Long proItemNo) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findExistingReservation(proItemNo);

        return reservationTimes.stream().map(reservationTime -> ExistingDateTimeResponse.builder()
                        .startDate(reservationTime.getStartDate())
                        .startTime(reservationTime.getStartTime())
                        .build())
                .toList();

    }

    public void isExist(ReservationRequest reservationRequest) {

        // proItem+startDate 으로 DB 조회
        List<ReservationTime> existingReservations = reservationTimeRepository.findExistingReservation(reservationRequest.getProItemNo(), reservationRequest.getStartDate());
        log.info(existingReservations.toString());

        // 조회된 List 에서 startTime 만 추출
        List<LocalTime> existingStartTimes = existingReservations.stream().map(ReservationTime::getStartTime).toList();
        log.info(existingStartTimes.toString());

        // reservationRequest 와 existingStartTimes 비교

        List<LocalTime> conflictingTimes = reservationRequest.getStartTimes().stream().filter(existingStartTimes::contains).toList();

        if (!conflictingTimes.isEmpty()) {
            throw new IllegalArgumentException("이미 예약된 시간입니다 : " + conflictingTimes.toString());
        }
    }

}
