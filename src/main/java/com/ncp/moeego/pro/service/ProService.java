package com.ncp.moeego.pro.service;

import com.ncp.moeego.pro.dto.ProApplyRequest;
import com.ncp.moeego.pro.dto.ProJoinRequest;

public interface ProService {
    String proJoin(ProJoinRequest request);
    String proApply(ProApplyRequest request);

}
