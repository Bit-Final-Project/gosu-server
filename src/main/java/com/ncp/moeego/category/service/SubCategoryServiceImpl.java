package com.ncp.moeego.category.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ncp.moeego.category.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import com.ncp.moeego.category.bean.SubCategoryDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService{
	
	private final SubCategoryRepository subCategoryRepository;

	@Override
    public List<SubCategoryDTO> getSubCategoriesByMainCategory(Long mainCateNo) {
        return subCategoryRepository.findByMainCateNo(mainCateNo)
                .stream()
                .map(subCategory -> new SubCategoryDTO(
                        subCategory.getSubCateNo(),
                        subCategory.getMainCateNo().getMainCateNo(), // MainCategory의 ID 추출
                        subCategory.getSubCateName()
                ))
                .collect(Collectors.toList());
    }


}
