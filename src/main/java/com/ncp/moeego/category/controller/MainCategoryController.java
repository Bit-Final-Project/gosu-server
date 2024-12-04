package com.ncp.moeego.category.controller;

import java.util.List;

import com.ncp.moeego.category.service.MainCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ncp.moeego.category.bean.MainCategoryDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MainCategoryController {

	private final MainCategoryService mainCategoryService;

	@GetMapping("/main_category")
	public ResponseEntity<List<MainCategoryDTO>> getMainCategory() {

		List<MainCategoryDTO> mainCategoryList = mainCategoryService.getMainCategoryList();

		return ResponseEntity.ok(mainCategoryList);

	}
}
