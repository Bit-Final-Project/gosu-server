package com.ncp.moeego.pro.repository;

import com.ncp.moeego.pro.bean.Pro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProRepository extends JpaRepository<Pro, Long> {

}
