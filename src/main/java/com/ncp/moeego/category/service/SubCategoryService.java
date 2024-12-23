package com.ncp.moeego.category.service;

import com.ncp.moeego.category.bean.SubCategoryDTO;
import com.ncp.moeego.category.entity.SubCategory;

import java.util.List;

public interface SubCategoryService {


    List<SubCategoryDTO> getSubCategoriesByMainCategory(Long mainCateNo);

    SubCategory getSubCategoryById(Long subCateNo);

}
