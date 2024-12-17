package com.ncp.moeego.category.repository;

import com.ncp.moeego.category.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {


    @Query("SELECT s FROM SubCategory s WHERE s.mainCategory.mainCateNo = :mainCateNo")
    public Collection<SubCategory> findByMainCateNo(@Param("mainCateNo") Long mainCateNo);


    SubCategory findBySubCateNo(Long subCateNo);
}
