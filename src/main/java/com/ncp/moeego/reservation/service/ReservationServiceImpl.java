package com.ncp.moeego.reservation.service;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.service.ProService;
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


    public ReservationServiceImpl(ReservationRepository reservationRepository, MemberService memberService, ProService proService, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.proService = proService;
        this.reservationTimeRepository = reservationTimeRepository;

    }

    @Transactional
    @Override
    public String makeReservation(ReservationRequest reservationRequest) {
        checkForConflictingReservations(reservationRequest);

        Reservation reservation = new Reservation();
        reservation.setMember(memberService.getMemberById(reservationRequest.getMemberNo()));
        reservation.setProItem(proService.getProItemById(reservationRequest.getProItemNo()));

        List<ReservationTime> reservationTimes = reservationRequest.getStartTimes().stream().map(startTime -> {
            ReservationTime reservationTime = new ReservationTime();
            reservationTime.setStartDate(reservationRequest.getStartDate());
            reservationTime.setStartTime(startTime);
            reservationTime.setReservation(reservation);
            return reservationTime;
        }).toList();

        reservation.getReservationTimes().addAll(reservationTimes);
        reservationRepository.save(reservation);

        return "예약 성공";
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExistingDateTimeResponse> getReservationByProItem(Long proItemNo) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findExistingReservation(proItemNo);
        return reservationTimes.stream().map(rt ->
                        ExistingDateTimeResponse.builder()
                                .startDate(rt.getStartDate())
                                .startTime(rt.getStartTime())
                                .build())
                .toList();
    }

    private void checkForConflictingReservations(ReservationRequest reservationRequest) {
        List<LocalTime> conflictingTimes = reservationRequest.getStartTimes().stream()
                .filter(startTime -> reservationTimeRepository.existsByReservation_ProItem_ProItemNoAndStartDateAndStartTime(
                        reservationRequest.getProItemNo(), reservationRequest.getStartDate(), startTime))
                .toList();

        if (!conflictingTimes.isEmpty()) {
            throw new IllegalArgumentException("이미 예약된 시간입니다: " + conflictingTimes);
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

        return reservations.stream().filter(reservation -> !reservation.getMember().getMemberNo().equals(pro.getMember().getMemberNo())).map(reservation -> ReceivedReservationResponse.builder()
                        .memberNo(reservation.getMember().getMemberNo())
                        .memberName(reservation.getMember().getName())
                        .proItemName(reservation.getProItem().getSubject())
                        .startDate(reservation.getReservationTimes().stream().map(ReservationTime::getStartDate).findFirst().orElse(null))
                        .startTimes(reservation.getReservationTimes().stream().map(ReservationTime::getStartTime).toList())
                        .build())
                .toList();
    }

    private List<MyReservationResponse> getMyReservations(Member member, Integer year) {

        List<Reservation> reservations = reservationRepository.findMyReservations(member.getMemberNo(), year);

        return reservations.stream().map(reservation -> MyReservationResponse.builder()
                        .proNo(reservation.getProItem().getPro().getProNo())
                        .proName(reservation.getProItem().getPro().getMember().getName())
                        .proItemNo(reservation.getProItem().getPro().getProNo())
                        .proItemName(reservation.getProItem().getSubject())
                        .reservationNo(reservation.getReservationNo())
                        .startDate(reservation.getReservationTimes().stream().map(ReservationTime::getStartDate).findFirst().orElse(null))
                        .startTimes(reservation.getReservationTimes().stream().map(ReservationTime::getStartTime).toList())
                        .build())
                .toList();
    }

    // 예약 수 조회
    @Override
    public Long getReservationCountByProItem(Long proItemNo) {
        Long count = reservationRepository.countReservationsByProItem(proItemNo);
        return count != null && count > 0 ? count : 0; // 예약이 없으면 0 반환
    }

    // 예약 취소
    @Override
    public String deleteReservation(String email, Long reservationNo) {
        Member member = memberService.getMemberByEmail(email);
        reservationRepository.deleteByMemberAndReservationNo(member, reservationNo);
        return "예약 취소 성공";

    }

}
