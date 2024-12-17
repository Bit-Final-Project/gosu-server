package com.ncp.moeego.category.service;

import com.ncp.moeego.category.bean.SubCategoryDTO;

import java.util.List;

public interface SubCategoryService {


    public List<SubCategoryDTO> getSubCategoriesByMainCategory(Long mainCateNo);

}
