package com.ncp.moeego.pro.service;

import com.ncp.moeego.category.entity.SubCategory;
import com.ncp.moeego.category.repository.MainCategoryRepository;
import com.ncp.moeego.category.repository.SubCategoryRepository;
import com.ncp.moeego.category.service.SubCategoryService;
import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.favorite.entity.Favorite;
import com.ncp.moeego.favorite.repository.FavoriteRepository;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.MemberService;
import com.ncp.moeego.pro.dto.*;
import com.ncp.moeego.pro.entity.ItemStatus;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.entity.ProItem;
import com.ncp.moeego.pro.repository.ProItemRepository;
import com.ncp.moeego.pro.repository.ProRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProServiceImpl implements ProService {

    private final MemberService memberService;
    private final SubCategoryService subCategoryService;
    private final ProRepository proRepository;
    private final ProItemRepository proItemRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;


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
            throw e;
        }
    }

    public void proJoinExecute(ProJoinRequest proJoinRequest) {
        JoinDTO join = new JoinDTO(proJoinRequest);
        log.info("Join 요청: {}", join);

        if (!memberService.write(join)) {
            throw new IllegalArgumentException("회원가입에 실패했습니다.");
        }

    }

    @Transactional
    @Override
    public ApiResponse updateIntro(String email, Map<String, String> payload) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            if (!member.getMemberStatus().equals(MemberStatus.ROLE_PRO)) {
                throw new IllegalArgumentException("달인 권한이 없습니다.");
            }
            Pro pro = proRepository.findByMember(member);
            pro.setIntro(payload.get("intro"));
            pro.setOneIntro(payload.get("oneIntro"));
            proRepository.save(pro);
            return ApiResponse.success("수정이 완료되었습니다.", null);
        } catch (Exception e) {
            return ApiResponse.error("수정 처리 중 오류가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    //고수 승인 요청
    @Override
    public ApiResponse proAccess(String email, ProApplyRequest proApplyRequest) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            proApplyRequest.setMemberNo(member.getMemberNo());
            proApplyExecute(proApplyRequest);
            return ApiResponse.success("신청이 완료되었습니다.", null);
        } catch (Exception e) {
            return ApiResponse.error("달인 신청 중 오류가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    public void proApplyExecute(ProApplyRequest proApplyRequest) {

        Pro pro = new Pro();
        pro.setMember(memberService.getMemberById(proApplyRequest.getMemberNo()));
        pro.setMainCategory(mainCategoryRepository.findById(proApplyRequest.getMainCateNo()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메인카테고리: " + proApplyRequest.getMainCateNo())));
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

        List<Long> proNoList = favoriteRepository.findProsByMemberNo(memberNo);

        if (proNoList.isEmpty()) {
            return Page.empty(pageable);
        }

        return proRepository.findByProNoIn(proNoList, pageable);
    }
    
    @Transactional
    @Override
    public String postFavorites(FavoritePostRequest favoritePostRequest) {

        Pro pro = getProById(favoritePostRequest.getProNo());
        Member member = memberService.getMemberById(favoritePostRequest.getMemberNo());
        if(member.getMemberNo().equals(pro.getMember().getMemberNo())){
            throw new IllegalArgumentException("자기 자신은 찜 할수 없습니다");
        }
        if (!favoriteRepository.findByProAndMember(pro, member).isEmpty()) {
            throw new IllegalArgumentException("이미 찜한 달인입니다.");
        }
        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setPro(pro);
        favoriteRepository.save(favorite);
        return "달인 찜하기 성공";
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

    public Pro getProByMemberNo(Long memberNo) {
        Member member = memberService.getMemberById(memberNo);
        Pro pro = proRepository.findByMember(member);
        if (pro == null) {
            throw new IllegalArgumentException("달인 검색 결과가 없습니다.");
        }
        return pro;
    }

    @Override
    public ItemDetailResponse getItemDetails(Long proItemNo) {


        if (!proItemRepository.existsByProItemNoAndItemStatus(proItemNo, ItemStatus.ACTIVE)) {
            throw new IllegalArgumentException("승인되지 않은 서비스입니다 : " + proItemNo + "번");

        }

        return proItemRepository.getItemDetails(proItemNo);

    }

    @Override
    public Map<String, Object> getInitItem(Long memberNo) {
        Member member = memberService.getMemberById(memberNo);
        if (!member.getMemberStatus().equals(MemberStatus.ROLE_PRO)) {
            throw new IllegalArgumentException(member.getName() + " 회원은 달인이 아닙니다.");
        }
        Pro pro = getProByMemberNo(memberNo);

        Map<String, Object> response = new HashMap<>();
        response.put("proNo", pro.getProNo());
        response.put("mainCategory", pro.getMainCategory());
        response.put("proItems", pro.getProItems().stream().toList());

        return response;
    }

    @Override
    public Map<String, Object> getItemList(Long subCateNo, String location, String value, int pg) {
        Pageable pageable = PageRequest.of(pg - 1, 5);
        Page<Pro> proPage = proRepository.findFilteredPros(MemberStatus.ROLE_PRO, pageable, subCateNo, location, value);

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

    @Override
    public Map<String, Object> getMainItem(Long subCateNo, String location, String value, int pg) {
        Pageable pageable = PageRequest.of(pg - 1, 300);
        Page<Pro> proPage = proRepository.findFilteredPros(MemberStatus.ROLE_PRO, pageable, subCateNo, location, value);

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

    @Override
    public ProItem getProItemById(Long proItemNo) {
        return proItemRepository.findById(proItemNo).orElseThrow(() -> new IllegalArgumentException("예약하려는 서비스가 없습니다 : " + proItemNo + "번 서비스"));

    }

    public Pro getProById(Long proNo) {
        return proRepository.findById(proNo).orElseThrow(() -> new IllegalArgumentException("해당 달인을 찾을 수 없습니다. proNo : " + proNo));
    }

}