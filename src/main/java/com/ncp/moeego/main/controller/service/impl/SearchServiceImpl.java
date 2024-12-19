package com.ncp.moeego.main.controller.service.impl;

import com.ncp.moeego.main.controller.service.SearchService;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.pro.dto.ItemResponse;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.repository.ProRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ProRepository proRepository;

    @Override
    public Map<String, Object> getSearchList(String value, int pg) {
        Pageable pageable = PageRequest.of(pg - 1, 3);
        Page<Pro> proPage = proRepository.findSearchValue(pageable, value);

        List<ItemResponse> proList = proPage.stream().map(pro -> new ItemResponse(
                pro.getProNo(),
                pro.getMember().getName(),
                pro.getMember().getProfileImage(),
                pro.getIntro(), pro.getOneIntro(),
                pro.getMainCategory().getMainCateNo(),
                pro.getMainCategory().getMainCateName(),
                pro.getStar(),
                pro.getReviewCount(),
                pro.getMember().getAddress(),
                pro.getProItems(),
                pro.getMember().getEmail(),
                pro.getMember().getPhone()
        )).toList();

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("content", proList); // 실제 데이터 리스트
        response.put("currentPage", proPage.getNumber()); // 현재 페이지
        response.put("totalPages", proPage.getTotalPages()); // 전체 페이지 수
        response.put("totalElements", proPage.getTotalElements()); // 전체 요소 수

        return response;
    }
}
