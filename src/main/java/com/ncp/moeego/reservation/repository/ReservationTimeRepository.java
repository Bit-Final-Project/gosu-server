package com.ncp.moeego.reservation.repository;

import com.ncp.moeego.reservation.entity.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
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
    
    boolean existsByReservation_ProItem_ProItemNoAndStartDateAndStartTime(
            Long proItemNo, 
            LocalDate startDate, 
            LocalTime startTime
    );


}
