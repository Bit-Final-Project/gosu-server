package com.ncp.moeego.category.bean;

import lombok.Data;

@Data
public class MainCategoryDTO {
	
	private Long mainCateNo;
    private String mainCateName;
	
    
    public MainCategoryDTO(Long mainCateNo, String mainCateName) {
    	this.mainCateNo = mainCateNo;
    	this.mainCateName = mainCateName;
    }
    
}
