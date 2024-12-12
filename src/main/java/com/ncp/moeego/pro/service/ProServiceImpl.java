package com.ncp.moeego.pro.service;

import com.ncp.moeego.category.repository.MainCategoryRepository;
import com.ncp.moeego.favorite.repository.FavoriteRepository;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.service.impl.MemberServiceImpl;
import com.ncp.moeego.pro.dto.FavoriteResponse;
import com.ncp.moeego.pro.dto.ProApplyRequest;
import com.ncp.moeego.pro.dto.ProJoinRequest;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.repository.ProRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProServiceImpl implements ProService {

    private final MemberServiceImpl memberService;
    private final ProRepository proRepository;

    public ProServiceImpl(MemberServiceImpl memberService, ProRepository proRepository, MainCategoryRepository mainCategoryRepository, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.proRepository = proRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.favoriteRepository = favoriteRepository;
    }

    private final MainCategoryRepository mainCategoryRepository;
    private final FavoriteRepository favoriteRepository;


    @Transactional
    @Override
    public String proJoin(ProJoinRequest proJoinRequest) {
        JoinDTO join = new JoinDTO(proJoinRequest);
        log.info(join.toString());

        if (!memberService.write(join)) {
            return "join fail";

        }

        ProApplyRequest proApplyRequest = new ProApplyRequest(proJoinRequest);
        proApplyRequest.setMemberNo(memberService.getMemberNo(proJoinRequest.getEmail()));

        String applyResponse = proApply(proApplyRequest);
        if (applyResponse.equals("pro apply success")) {
            return "pro join success";
        }
        return "pro apply fail";
    }

    @Override
    public String proApply(ProApplyRequest request) {
        Pro pro = new Pro();
        pro.setMember(memberService.getMemberById(request.getMemberNo()));
        pro.setMainCateNo(mainCategoryRepository.findById(request.getMainCateNo()).orElseThrow(() -> new IllegalArgumentException("Invalid MainCateNo")));
        pro.setSubCategories(request.getSubCategories());
        pro.setOneIntro(request.getOneIntro());
        pro.setIntro(request.getIntro());
        proRepository.save(pro);
        memberService.setMemberStatus(request.getMemberNo(), MemberStatus.ROLE_PEND_PRO);

        return "pro apply success";
    }

    @Override
    public Page<FavoriteResponse> getFavorites(Long memberNo, int pg) {

        Pageable pageable = PageRequest.of(pg - 1, 10);

        List<Long> proNoList = favoriteRepository.findProNosByMemberNo(memberNo);

        if (proNoList.isEmpty()) {
            return Page.empty(pageable);
        }

        return proRepository.findByProNoIn(proNoList, pageable);
    }

}
