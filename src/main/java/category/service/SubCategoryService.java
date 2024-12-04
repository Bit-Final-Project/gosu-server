package category.service;

import java.util.List;

import category.bean.SubCategoryDTO;

public interface SubCategoryService {


	public List<SubCategoryDTO> getSubCategoriesByMainCategory(Long mainCateNo);

}
