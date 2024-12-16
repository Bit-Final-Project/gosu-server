package com.ncp.moeego.pro.repository;

import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.pro.dto.FavoriteResponse;
import com.ncp.moeego.pro.entity.Pro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProRepository extends JpaRepository<Pro, Long> {

    @Query(
            """
                    SELECT new com.ncp.moeego.pro.dto.FavoriteResponse(
                                p.proNo, p.member.name, p.member.profileImage,p.mainCateNo.mainCateNo,
                                p.star, p.oneIntro
                                )
                    From Pro p
                    Where p.proNo IN :proNo
                    
                    """
    )
    Page<FavoriteResponse> findByProNoIn(@Param("proNo") List<Long> proNo, Pageable pageable);

	Pro findByMember(Member member);

}
