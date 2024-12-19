package com.ncp.moeego.reservation.service;

import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.pro.service.ProService;
import com.ncp.moeego.reservation.dto.ReservationRequest;
import com.ncp.moeego.reservation.entity.Reservation;
import com.ncp.moeego.reservation.entity.ReservationTime;
import com.ncp.moeego.reservation.repository.ReservationRepository;
import com.ncp.moeego.reservation.repository.ReservationTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final MemberService memberService;
    private final ProService proService;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationTimeRepository timeRepository, MemberService memberService, ProService proService, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.memberService = memberService;
        this.proService = proService;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    @Override
    public void makeReservation(ReservationRequest reservationRequest) {
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
    }

    public void isExist(ReservationRequest reservationRequest) {

    }
}
