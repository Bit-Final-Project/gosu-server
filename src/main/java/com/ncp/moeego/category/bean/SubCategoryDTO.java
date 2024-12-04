package com.ncp.moeego.category.bean;

import lombok.Data;

@Data
public class SubCategoryDTO {

	private Long subCateNo;
	private Long mainCateNo;
	private String subCateName;
	
	public SubCategoryDTO(Long subCateNo, Long mainCateNo, String subCateName) {
		this.subCateNo = subCateNo;
		this.mainCateNo = mainCateNo;
		this.subCateName = subCateName;
	}
}
