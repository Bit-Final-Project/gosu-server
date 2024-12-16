package com.ncp.moeego.pro.service;

import com.ncp.moeego.category.bean.SubCategory;
import com.ncp.moeego.category.repository.MainCategoryRepository;
import com.ncp.moeego.category.repository.SubCategoryRepository;
import com.ncp.moeego.category.service.SubCategoryServiceImpl;
import com.ncp.moeego.favorite.repository.FavoriteRepository;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.service.impl.MemberServiceImpl;
import com.ncp.moeego.pro.dto.FavoriteResponse;
import com.ncp.moeego.pro.dto.PostItemRequest;
import com.ncp.moeego.pro.dto.ProApplyRequest;
import com.ncp.moeego.pro.dto.ProJoinRequest;
import com.ncp.moeego.pro.entity.ItemStatus;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.entity.ProItem;
import com.ncp.moeego.pro.repository.ProItemRepository;
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
    private final SubCategoryServiceImpl subCategoryService;
    private final ProRepository proRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final FavoriteRepository favoriteRepository;
    private final ProItemRepository proItemRepository;
    private final SubCategoryRepository subCategoryRepository;

    public ProServiceImpl(MemberServiceImpl memberService, SubCategoryServiceImpl subCategoryService, ProRepository proRepository, MainCategoryRepository mainCategoryRepository, FavoriteRepository favoriteRepository, ProItemRepository proItemRepository, SubCategoryRepository subCategoryRepository) {
        this.memberService = memberService;
        this.subCategoryService = subCategoryService;
        this.proRepository = proRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.favoriteRepository = favoriteRepository;
        this.proItemRepository = proItemRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @Transactional
    @Override
    public String proJoin(ProJoinRequest proJoinRequest) {
        try {
            proJoinExecute(proJoinRequest);
            ProApplyRequest proApplyRequest = new ProApplyRequest(proJoinRequest);
            proApplyRequest.setMemberNo(memberService.getMemberNo(proJoinRequest.getEmail()));
            proApplyExecute(proApplyRequest);
            return "달인 회원가입 성공";

        } catch (Exception e) {
            log.error("회원가입 실패 : {}", e.getMessage());
            return "회원가입 실패";

        }
    }

    @Transactional
    @Override
    public String proApply(ProApplyRequest request) {
        return "";
    }

    public void proJoinExecute(ProJoinRequest proJoinRequest) {
        JoinDTO join = new JoinDTO(proJoinRequest);
        log.info("Join 요청: {}", join);

        if (!memberService.write(join)) {
            throw new IllegalArgumentException("회원가입에 실패했습니다.");
        }

    }

    public void proApplyExecute(ProApplyRequest proApplyRequest) {

        Pro pro = new Pro();
        pro.setMember(memberService.getMemberById(proApplyRequest.getMemberNo()));
        pro.setMainCateNo(mainCategoryRepository.findById(proApplyRequest.getMainCateNo()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메인카테고리: " + proApplyRequest.getMainCateNo())));
        pro.setOneIntro(proApplyRequest.getOneIntro());
        pro.setIntro(proApplyRequest.getIntro());

        for (Long subCateNo : proApplyRequest.getSubCategories()) {
            ProItem proItem = new ProItem();
            proItem.setPro(pro);
            proItem.setSubCategory(subCategoryService.getSubCategoryById(subCateNo));
            proItem.setItemStatus(ItemStatus.PENDING);
            pro.getProItems().add(proItem);

        }

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

    @Transactional
    @Override
    public String postItem(PostItemRequest postItemRequest) {

        SubCategory subCategory = subCategoryRepository.findById(postItemRequest.getSubCateNo()).orElseThrow(() -> new IllegalArgumentException("올바르지 않은 서브카테고리입니다 : " + postItemRequest.getSubCateNo()));

        ProItem proItem = proItemRepository.findProItemsByPro_ProNoAndSubCategory(postItemRequest.getProNo(), subCategory);
        log.info("proItem : {}", proItem);

        if (!proItem.getItemStatus().equals(ItemStatus.ACTIVE)) {
            throw new IllegalArgumentException("승인되지 않은 서비스입니다.");
        }

        proItem.setSubject(postItemRequest.getSubject());
        proItem.setContent(postItemRequest.getContent());
        proItem.setPrice(postItemRequest.getPrice());

        proItemRepository.save(proItem);

        return "달인 서비스 등록 성공";
    }

}