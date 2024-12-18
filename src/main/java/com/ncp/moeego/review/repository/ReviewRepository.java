package com.ncp.moeego.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ncp.moeego.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

}
