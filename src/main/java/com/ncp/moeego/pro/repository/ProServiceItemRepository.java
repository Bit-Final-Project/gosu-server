package com.ncp.moeego.pro.repository;

import com.ncp.moeego.pro.entity.ProServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProServiceItemRepository extends JpaRepository<ProServiceItem, Long> {
}
