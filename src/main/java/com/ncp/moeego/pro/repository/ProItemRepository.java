package com.ncp.moeego.pro.repository;

import com.ncp.moeego.category.bean.SubCategory;
import com.ncp.moeego.pro.dto.ItemResponse;
import com.ncp.moeego.pro.entity.ItemStatus;
import com.ncp.moeego.pro.entity.ProItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProItemRepository extends JpaRepository<ProItem, Long> {

    ProItem findProItemsByPro_ProNoAndSubCategory(Long proProNo, SubCategory subCategory);

    @Query(
            """
                    select new com.ncp.moeego.pro.dto.ItemResponse(
                    p.pro.member.name, p.pro.member.profileImage, p.pro.member.address, p.subCategory.mainCateNo.mainCateName, p.subCategory.subCateName, p.subject, p.content, p.price, p.pro.star
                    )
                    from ProItem p
                    where p.proItemNo =:proItemNo
                    """)
    ItemResponse getItemDetails(@Param("proItemNo") Long proItemNo);

    boolean existsByProItemNoAndItemStatusAndItemStatus(Long proItemNo, ItemStatus itemStatus, ItemStatus itemStatus1);

    boolean existsByProItemNoAndItemStatus(Long proItemNo, ItemStatus itemStatus);
}
