package com.ncp.moeego.reservation.repository;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.reservation.entity.Reservation;
import com.ncp.moeego.reservation.entity.ReservationTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
                select r from Reservation r, ReservationTime rt
                where rt.reservation.member.memberNo =:memberNo
                and YEAR(rt.startDate) = :year
            """)
    List<Reservation> findMyReservations(@Param("memberNo") Long memberNo, @Param("year") Integer year);

    @Query("""
                   select r from Reservation r, ReservationTime rt
                   where rt.reservation.proItem.pro.proNo =:proNo
                   and YEAR(rt.startDate) = :year
            """)
    List<Reservation> findReceivedReservations(@Param("proNo") Long proNo, @Param("year") Integer year);
           
	// Get count of reservations for a given proItemNo
	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.proItem.proItemNo = :proItemNo")
	Long countReservationsByProItem(@Param("proItemNo") Long proItemNo);

    void deleteByMemberAndReservationNo(Member member, Long reservationNo);
}
