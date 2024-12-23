package com.ncp.moeego.category.service;

import com.ncp.moeego.category.bean.SubCategoryDTO;
import com.ncp.moeego.category.entity.SubCategory;

import java.util.List;

public interface SubCategoryService {


    public List<SubCategoryDTO> getSubCategoriesByMainCategory(Long mainCateNo);

    SubCategory getSubCategoryById(Long subCateNo);

}
