package com.ncp.moeego.pro.service;

import com.ncp.moeego.pro.dto.FavoriteResponse;
import com.ncp.moeego.pro.dto.ProApplyRequest;
import com.ncp.moeego.pro.dto.ProJoinRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProService {
    String proJoin(ProJoinRequest request);

    void proApply(ProApplyRequest request);

    Page<FavoriteResponse> getFavorites(Long memberNo, int pg);

    String deleteFavorites(Long memNo, List<Long> proNo);
}
