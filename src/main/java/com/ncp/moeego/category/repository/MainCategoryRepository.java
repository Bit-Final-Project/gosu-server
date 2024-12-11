package com.ncp.moeego.category.repository;

import com.ncp.moeego.category.bean.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory, Long>{

}
