package com.ncp.moeego.pro.service;

import com.ncp.moeego.pro.dto.FavoriteResponse;
import com.ncp.moeego.pro.dto.ProApplyRequest;
import com.ncp.moeego.pro.dto.ProJoinRequest;
import org.springframework.data.domain.Page;

public interface ProService {
    String proJoin(ProJoinRequest request);

    String proApply(ProApplyRequest request);

    Page<FavoriteResponse> getFavorites(Long memberNo, int pg);
}
