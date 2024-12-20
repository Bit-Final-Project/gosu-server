package com.ncp.moeego.reservation.repository;

import com.ncp.moeego.reservation.entity.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
    @Query("""
            select rt from ReservationTime rt
            where rt.reservation.proItem.proItemNo =:proItemNo and
            rt.startDate = :startDate
            """)
    List<ReservationTime> findExistingReservation(@Param("proItemNo") Long proItemNo, @Param("startDate") LocalDate startDate);

    @Query("""
                select rt from ReservationTime rt
                where rt.reservation.proItem.proItemNo =:proItemNo
            """)
    List<ReservationTime> findExistingReservation(@Param("proItemNo") Long proItemNo);

    @Query("""
                   select rt from ReservationTime rt
                   where rt.reservation.proItem.pro.proNo =:proNo
                   and YEAR(rt.startDate) = :year
            """)
    List<ReservationTime> findReceivedReservations(@Param("proNo") Long proNo, @Param("year") Integer year);

    @Query("""
                select rt from ReservationTime rt
                where rt.reservation.member.memberNo =:memberNo
                and YEAR(rt.startDate) = :year
            """)
    List<ReservationTime> findMyReservations(@Param("memberNo") Long memberNo, @Param("year") Integer year);
}
