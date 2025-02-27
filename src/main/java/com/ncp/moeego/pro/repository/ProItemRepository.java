package com.ncp.moeego.pro.repository;

import com.ncp.moeego.category.entity.SubCategory;
import com.ncp.moeego.pro.dto.ItemDetailResponse;
import com.ncp.moeego.pro.entity.ItemStatus;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.entity.ProItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProItemRepository extends JpaRepository<ProItem, Long> {

    ProItem findProItemsByPro_ProNoAndSubCategory(Long proProNo, SubCategory subCategory);

    @Query(
            """
                    select new com.ncp.moeego.pro.dto.ItemDetailResponse(
                    p.pro.member.name, p.pro.member.profileImage, p.pro.member.address, p.subCategory.mainCategory.mainCateName, p.subCategory.subCateName, p.subject, p.content, p.price, p.pro.star
                    )
                    from ProItem p
                    where p.proItemNo =:proItemNo
                    """)
    ItemDetailResponse getItemDetails(@Param("proItemNo") Long proItemNo);

    boolean existsByProItemNoAndItemStatus(Long proItemNo, ItemStatus itemStatus);
    
    // 해당 Pro에 속하는 ProItem 조회 (admin에서 사용)
    List<ProItem> findByPro(Pro pro);  
    
    List<ProItem> findByPro_Member_MemberNo(Long memberNo);

	List<ProItem> findByPro_ProNo(Long proNo);
	
	// ProItem 업데이트
	@Modifying
	@Query("UPDATE ProItem p SET p.reviewCount = :reviewCount, p.star = :averageStar WHERE p.proItemNo = :proItemNo")
	void updateStatistics(@Param("proItemNo") Long proItemNo, @Param("reviewCount") int reviewCount, @Param("averageStar") float averageStar);

	// Pro의 reviewCount 합계 (null일 경우 0 반환)
	@Query("SELECT COALESCE(SUM(p.reviewCount), 0) FROM ProItem p WHERE p.pro.proNo = :proNo")
	int sumReviewCountByProNo(@Param("proNo") Long proNo);

	// Pro의 star 합계 (null일 경우 0 반환)
	@Query("SELECT COALESCE(SUM(p.star * p.reviewCount), 0) FROM ProItem p WHERE p.pro.proNo = :proNo")
	float sumStarByProNo(@Param("proNo") Long proNo);
}
