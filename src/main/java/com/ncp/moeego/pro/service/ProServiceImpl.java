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
        log.info("Join 요청: {}", join);


        if (!memberService.write(join)) {
            throw new IllegalArgumentException("회원 가입에 실패했습니다.");
        }

        ProApplyRequest proApplyRequest = new ProApplyRequest(proJoinRequest);
        proApplyRequest.setMemberNo(memberService.getMemberNo(proJoinRequest.getEmail()));

        proApply(proApplyRequest);

        return "달인회원 가입 성공.";

    }

    @Override
    public void proApply(ProApplyRequest proApplyRequest) {
        Pro pro = new Pro();
        pro.setMember(memberService.getMemberById(proApplyRequest.getMemberNo()));
        pro.setMainCateNo(mainCategoryRepository.findById(proApplyRequest.getMainCateNo()).orElseThrow(() -> new IllegalArgumentException("Invalid MainCateNo")));
        pro.setSubCategories(proApplyRequest.getSubCategories());
        pro.setOneIntro(proApplyRequest.getOneIntro());
        pro.setIntro(proApplyRequest.getIntro());
        proRepository.save(pro);
        memberService.setMemberStatus(proApplyRequest.getMemberNo(), MemberStatus.ROLE_PEND_PRO);

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

    @Transactional
    @Override
    public String deleteFavorites(Long memberNo, List<Long> proNo) {
        log.info("deleteFavorites : {}, {}", memberNo, proNo.toString());
        int deletedRows = favoriteRepository.deleteFavorite(memberNo, proNo);

        if (deletedRows == 0) {
            throw new IllegalArgumentException("삭제할 항목이 없습니다.");

        }

        return "찜한 달인 삭제 성공";


    }

}