package com.ncp.moeego.reservation.repository;

import com.ncp.moeego.reservation.entity.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
}
