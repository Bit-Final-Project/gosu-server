package com.ncp.moeego.category.service;

import java.util.List;

import com.ncp.moeego.category.bean.SubCategoryDTO;

public interface SubCategoryService {


	public List<SubCategoryDTO> getSubCategoriesByMainCategory(Long mainCateNo);

}
