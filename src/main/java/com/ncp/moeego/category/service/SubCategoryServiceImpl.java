package com.ncp.moeego.category.service;

import com.ncp.moeego.category.bean.SubCategoryDTO;
import com.ncp.moeego.category.entity.SubCategory;
import com.ncp.moeego.category.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    @Override
    public List<SubCategoryDTO> getSubCategoriesByMainCategory(Long mainCateNo) {
        return subCategoryRepository.findByMainCateNo(mainCateNo)
                .stream()
                .map(subCategory -> new SubCategoryDTO(
                        subCategory.getSubCateNo(),
                        subCategory.getMainCategory().getMainCateNo(), // MainCategory의 ID 추출
                        subCategory.getSubCateName()
                ))
                .collect(Collectors.toList());
    }

    public SubCategory getSubCategoryById(Long subCateNo) {
        return subCategoryRepository.findById(subCateNo).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 서브카테고리: " + subCateNo));
    }

}
