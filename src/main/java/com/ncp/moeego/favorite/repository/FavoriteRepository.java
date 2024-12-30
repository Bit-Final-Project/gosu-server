package com.ncp.moeego.favorite.repository;

import com.ncp.moeego.favorite.entity.Favorite;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.pro.entity.Pro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT f.pro.proNo FROM Favorite f WHERE f.member.memberNo = :memberNo AND f.pro.member.memberStatus = 'ROLE_PRO'")
    List<Long> findProsByMemberNo(@Param("memberNo") Long memberNo);

    @Modifying
    @Query("DELETE From Favorite f where f.pro.proNo in :proNo and f.member.memberNo =:memberNo")
    int deleteFavorite(@Param("memberNo") Long memberNo, @Param("proNo") List<Long> proNo);
    
    List<Favorite> findByProAndMember(Pro pro, Member member);

}
