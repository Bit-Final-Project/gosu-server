package com.ncp.moeego.pro.service;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.member.bean.SignOutDTO;
import com.ncp.moeego.pro.dto.*;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.entity.ProItem;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface ProService {
    String proJoin(ProJoinRequest proJoinRequest);

    ApiResponse updateIntro(String email, Map<String, String> payload);

    ApiResponse proAccess(String email, ProApplyRequest proApplyRequest);

    Page<FavoriteResponse> getFavorites(Long memberNo, int pg);

    String deleteFavorites(Long memNo, List<Long> proNo);

    String postItem(PostItemRequest postItemRequest);

    ItemDetailResponse getItemDetails(Long proItemNo);

    Map<String, Object> getInitItem(Long memberNo);

    Map<String, Object> getItemList(Long subCateNo, String location, String value, int pg);

    ProItem getProItemById(Long proItemNo);

    String postFavorites(FavoritePostRequest favoritePostRequest);

    Pro getProByMemberNo(Long memberNo);

    Object getMainItem(Long subCateNo, String location, String value, int pg);
}
