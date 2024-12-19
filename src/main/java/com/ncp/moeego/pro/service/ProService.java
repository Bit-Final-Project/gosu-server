package com.ncp.moeego.pro.service;

import com.ncp.moeego.pro.dto.*;
import com.ncp.moeego.pro.entity.ProItem;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ProService {
    String proJoin(ProJoinRequest proJoinRequest);

    String proApply(ProApplyRequest request);

    Page<FavoriteResponse> getFavorites(Long memberNo, int pg);

    String deleteFavorites(Long memNo, List<Long> proNo);

    String postItem(PostItemRequest postItemRequest);

    Map<String, Object> getInitItem(Long memberNo);

    Map<String, Object> getItemList(Long subCateNo, String location, int pg);

    ProItem getProItemById(Long proItemNo);
}
