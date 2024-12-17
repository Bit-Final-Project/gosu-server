package com.ncp.moeego.pro.service;

import com.ncp.moeego.pro.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProService {
    String proJoin(ProJoinRequest proJoinRequest);

    String proApply(ProApplyRequest request);

    Page<FavoriteResponse> getFavorites(Long memberNo, int pg);

    String deleteFavorites(Long memNo, List<Long> proNo);

    String postItem(PostItemRequest postItemRequest);

    ItemResponse getItemDetails(Long proItemNo);
}
