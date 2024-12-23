package com.ncp.moeego.reservation.repository;

import com.ncp.moeego.reservation.entity.Reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	// Get count of reservations for a given proItemNo
	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.proItem.proItemNo = :proItemNo")
	Long countReservationsByProItem(@Param("proItemNo") Long proItemNo);

}
