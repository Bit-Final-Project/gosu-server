package com.ncp.moeego.category.controller;

import com.ncp.moeego.category.bean.MainCategoryDTO;
import com.ncp.moeego.category.service.MainCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
