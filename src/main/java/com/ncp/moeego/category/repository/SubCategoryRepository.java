package com.ncp.moeego.category.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ncp.moeego.category.bean.SubCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {


	@Query("SELECT s FROM SubCategory s WHERE s.mainCateNo.mainCateNo = :mainCateNo")
	public Collection<SubCategory> findByMainCateNo(@Param("mainCateNo") Long mainCateNo);



}
