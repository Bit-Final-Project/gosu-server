package com.ncp.moeego.category.controller;

import java.util.List;

import com.ncp.moeego.category.service.SubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ncp.moeego.category.bean.SubCategoryDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SubCategoryController {
	
	private final SubCategoryService subCategoryService;

	
    @GetMapping("/sub_category/{mainCateNo}")
    public ResponseEntity<List<SubCategoryDTO>> getSubCategoriesByMainCategory(
            @PathVariable("mainCateNo") Long mainCateNo) {
        
        // 메인 카테고리 번호 검증
        if (mainCateNo < 1 || mainCateNo > 6) {
            return ResponseEntity.badRequest().build(); // 잘못된 번호 처리
        }

        // 서비스 호출하여 데이터 가져오기
        List<SubCategoryDTO> subCategoryList = subCategoryService.getSubCategoriesByMainCategory(mainCateNo);

        return ResponseEntity.ok(subCategoryList);
    }
	
}
