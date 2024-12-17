package com.ncp.moeego.favorite.repository;

import com.ncp.moeego.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("Select f.pro.proNo FROM Favorite f where f.member.memberNo =:memberNo")
    List<Long> findProsByMemberNo(@Param("memberNo") Long memberNo);

    @Modifying
    @Query("DELETE From Favorite f where f.pro.proNo in :proNo and f.member.memberNo =:memberNo")
    int deleteFavorite(@Param("memberNo") Long memberNo, @Param("proNo") List<Long> proNo);

}
