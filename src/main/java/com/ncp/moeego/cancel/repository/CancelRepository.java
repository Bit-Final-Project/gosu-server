package com.ncp.moeego.cancel.repository;

import com.ncp.moeego.cancel.entity.Cancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelRepository extends JpaRepository<Cancel, Long> {

}
