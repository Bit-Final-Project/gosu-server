package com.ncp.moeego.pro.service;

import com.ncp.moeego.category.repository.MainCategoryRepository;
import com.ncp.moeego.category.repository.SubCategoryRepository;
import com.ncp.moeego.member.bean.JoinDTO;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.service.impl.MemberServiceImpl;
import com.ncp.moeego.pro.bean.Pro;
import com.ncp.moeego.pro.dto.ProApplyRequest;
import com.ncp.moeego.pro.dto.ProJoinRequest;
import com.ncp.moeego.pro.repository.ProRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ProServiceImpl implements ProService {

    private final MemberServiceImpl memberService;
    private final ProRepository proRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public ProServiceImpl(MemberServiceImpl memberService, ProRepository proRepository, MainCategoryRepository mainCategoryRepository, SubCategoryRepository subCategoryRepository) {
        this.memberService = memberService;
        this.proRepository = proRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

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
        pro.setMainCateNo(mainCategoryRepository.findById(request.getMainCateNo()).orElseThrow(() -> new IllegalArgumentException("Invalid MainCateNo")));
        pro.setSubCateNo(subCategoryRepository.findById(request.getSubCateNo()).orElseThrow(() -> new IllegalArgumentException("Invalid SubCateNo")));
        pro.setOneIntro(request.getOneIntro());
        pro.setIntro(request.getIntro());
        proRepository.save(pro);
        memberService.setMemberStatus(request.getMemberNo(), MemberStatus.ROLE_PEND_PRO);

        return "pro apply success";
    }

}
