package com.ncp.moeego.pro.repository;

import com.ncp.moeego.category.bean.SubCategory;
import com.ncp.moeego.pro.entity.ProItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProItemRepository extends JpaRepository<ProItem, Long> {

    ProItem findProItemsByPro_ProNoAndSubCategory(Long proProNo, SubCategory subCategory);
}
