package com.ncp.moeego.reservation.service;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.service.ProService;
import com.ncp.moeego.pro.service.ProServiceImpl;
import com.ncp.moeego.reservation.dto.ExistingDateTimeResponse;
import com.ncp.moeego.reservation.dto.MyReservationResponse;
import com.ncp.moeego.reservation.dto.ReceivedReservationResponse;
import com.ncp.moeego.reservation.dto.ReservationRequest;
import com.ncp.moeego.reservation.entity.Reservation;
import com.ncp.moeego.reservation.entity.ReservationTime;
import com.ncp.moeego.reservation.repository.ReservationRepository;
import com.ncp.moeego.reservation.repository.ReservationTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final MemberService memberService;
    private final ProService proService;
    private final ProServiceImpl proServiceImpl;

    public ReservationServiceImpl(ReservationRepository reservationRepository, MemberService memberService, ProService proService, ReservationTimeRepository reservationTimeRepository, ProServiceImpl proServiceImpl) {
        this.reservationRepository = reservationRepository;

        this.memberService = memberService;
        this.proService = proService;
        this.reservationTimeRepository = reservationTimeRepository;
        this.proServiceImpl = proServiceImpl;
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

    @Override
    public Map<String, Object> getReservations(String email, Integer year) {
        Map<String, Object> response = new HashMap<>();

        Member member = memberService.getMemberByEmail(email);
        switch (member.getMemberStatus()) {
            case ROLE_PRO -> {
                List<ReceivedReservationResponse> receivedReservations = getReceivedReservations(proService.getProByMemberNo(member.getMemberNo()), year);
                List<MyReservationResponse> myReservations = getMyReservations(member, year);
                response.put("receivedReservations", receivedReservations);
                response.put("myReservations", myReservations);
            }
            case ROLE_USER -> {
                List<MyReservationResponse> myReservations = getMyReservations(member, year);
                response.put("myReservations", myReservations);
            }

            default -> throw new IllegalArgumentException("예약 내역을 조회할 수 없는 회원입니다.");


        }

        return response;
    }

    private List<ReceivedReservationResponse> getReceivedReservations(Pro pro, Integer year) {
        List<Reservation> reservations = reservationRepository.findReceivedReservations(pro.getProNo(), year);
        List<ReceivedReservationResponse> receivedReservations = reservations.stream().map(reservation -> ReceivedReservationResponse.builder()
                        .memberNo(reservation.getMember().getMemberNo())
                        .memberName(reservation.getMember().getName())
                        .proItemName(reservation.getProItem().getSubject())
                        .startDate(reservation.getReservationTimes().stream().map(reservationTime -> reservationTime.getStartDate()).findFirst().orElse(null))
                        .startTimes(reservation.getReservationTimes().stream().map(reservationTime -> reservationTime.getStartTime()).toList())
                        .build())
                .toList();
        return receivedReservations;

    }

    private List<MyReservationResponse> getMyReservations(Member member, Integer year) {
        List<Reservation> reservations = reservationRepository.findMyReservations(member.getMemberNo(), year);
        List<MyReservationResponse> myReservations = reservations.stream().map(reservation -> MyReservationResponse.builder()
                        .proNo(reservation.getProItem().getPro().getProNo())
                        .proName(reservation.getProItem().getPro().getMember().getName())
                        .proItemNo(reservation.getProItem().getPro().getProNo())
                        .proItemName(reservation.getProItem().getSubject())
                        .startDate(reservation.getReservationTimes().stream().map(reservationTime -> reservationTime.getStartDate()).findFirst().orElse(null))
                        .startTimes(reservation.getReservationTimes().stream().map(reservationTime -> reservationTime.getStartTime()).toList())
                        .build())
                .toList();
        return myReservations;
    }


}
