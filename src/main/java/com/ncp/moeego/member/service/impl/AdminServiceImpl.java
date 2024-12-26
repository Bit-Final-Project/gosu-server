package com.ncp.moeego.member.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.member.bean.ArticleImageDTO;
import com.ncp.moeego.member.bean.CancelDTO;
import com.ncp.moeego.article.bean.ArticleDTO;
import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.article.repository.ArticleRepository;
import com.ncp.moeego.cancel.entity.Cancel;
import com.ncp.moeego.image.entity.Image;
import com.ncp.moeego.image.repository.ImageRepository;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.ProDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.controller.AdminController;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.member.service.AdminService;
import com.ncp.moeego.ncp.service.ObjectStorageService;
import com.ncp.moeego.pro.entity.ItemStatus;
import com.ncp.moeego.pro.entity.Pro;
import com.ncp.moeego.pro.entity.ProItem;
import com.ncp.moeego.pro.repository.ProItemRepository;
import com.ncp.moeego.pro.repository.ProRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final ProRepository proRepository;
    private final ProItemRepository proItemRepository;
    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;

    // 네이버 클라우드
    private final ObjectStorageService objectStorageService;

    private String bucketName = "moeego";
    
    @Override
    public int getRoleUserCount() {
        return memberRepository.countByMemberStatus(MemberStatus.ROLE_USER);
    }

    @Override
    public int getRoleCancelCount() {
        return memberRepository.countByMemberStatus(MemberStatus.ROLE_CANCEL);
    }

	@Override
	public int getRoleProCount() {
		return memberRepository.countByMemberStatus(MemberStatus.ROLE_PRO);
	}

	@Override
	public List<MemberSummaryDTO> getPendingProMembers(Pageable pageable, MemberStatus status) {
	    return memberRepository.findMemberSummaryByStatus(pageable,status);
	}

	@Override
	public boolean approveMember(Long member_no) {
	    // 1. Member 테이블에서 멤버 조회
	    Member member = memberRepository.findById(member_no).orElse(null);
	    log.info("member : " + member);
	    if (member != null && member.getMemberStatus() == MemberStatus.ROLE_PEND_PRO) {
	        
	        // 2. Member 테이블에서 상태를 ROLE_PRO로 변경
	        member.setMemberStatus(MemberStatus.ROLE_PRO);
	        member.setEmailStatus(1);
	        memberRepository.save(member);  // 변경된 멤버 저장

	        // 3. Pro 엔티티의 accessDate 값도 현재 날짜로 설정
	        Pro pro = proRepository.findByMember(member);  // 해당 멤버의 Pro 객체 찾기
	        if (pro != null) {
	            pro.setAccessDate(LocalDateTime.now());  // accessDate를 현재 날짜로 설정
	            proRepository.save(pro);  // Pro 엔티티 저장
	        }

	        // 4. ProItem 테이블에서 상태를 ACTIVE로 변경
	        List<ProItem> proItems = proItemRepository.findByPro(pro);  // 해당 Pro에 관련된 모든 ProItem 찾기
	        for (ProItem proItem : proItems) {
	            proItem.setItemStatus(ItemStatus.ACTIVE);  // 상태를 ACTIVE로 설정
	            proItemRepository.save(proItem);  // ProItem 상태 저장
	        }

	        return true;
	    }
	    return false;
	}
	
	@Override
	public boolean cancelMember(Long memberNo) {
	    // Member 객체 가져오기
	    Member member = memberRepository.findById(memberNo).orElse(null);

	    if (member == null) {
	        return false; // Member가 없으면 취소 실패
	    }

	    // Pro 조회 (Member와 연결된 Pro 데이터)
	    Pro pro = proRepository.findByMember_MemberNo(memberNo);

	    if (pro != null) {
	        // ProItem 삭제 (Pro와 연결된 모든 ProItem 삭제)
	        List<ProItem> proItems = proItemRepository.findByPro_ProNo(pro.getProNo());
	        if (!proItems.isEmpty()) {
	            proItemRepository.deleteAll(proItems);
	        }

	        // Pro 삭제
	        proRepository.delete(pro);
	    }

	    // Member 상태 변경
	    member.setMemberStatus(MemberStatus.ROLE_USER); // 상태 변경
	    memberRepository.save(member); // Member 상태 변경 후 저장
	    member.setEmailStatus(1);
	    return true; // 작업 성공
	}


	@Override
	public List<Map<String, Object>> getWeekMemberData() {
		LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);

        LocalDateTime startDateTime = weekAgo.atStartOfDay();  // 일주일 전 자정
        LocalDateTime endDateTime = today.atStartOfDay().plusDays(1).minusNanos(1);
        
        // 회원 가입 데이터 조회 (가입 날짜가 일주일 이내인 회원들)
        List<Member> members = memberRepository.findByJoinDateBetween(startDateTime, endDateTime);

        // 날짜별로 가입 수 집계
        return members.stream()
                .collect(Collectors.groupingBy(member -> member.getJoinDate().toLocalDate()))
                .entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> result = Map.of(
                        "date", entry.getKey().format(DateTimeFormatter.ISO_DATE),
                        "count", entry.getValue().size()
                    );
                    return result;
                })
                .collect(Collectors.toList());
	}

	@Override
	public List<Map<String, Object>> getProMemberJoinData(LocalDateTime startDateTime, LocalDateTime endDateTime) {
	    List<Pro> proMembers = memberRepository.findProMembersByJoinDate(startDateTime, endDateTime);

	    return proMembers.stream()
	        .collect(Collectors.groupingBy(
	            member -> member.getAccessDate().toLocalDate(), // 날짜별로 그룹핑
	            Collectors.counting() // 각 날짜별 개수 계산
	        ))
	        .entrySet()
	        .stream()
	        .map(entry -> {
	            Map<String, Object> result = Map.of(
	                "date", entry.getKey().format(DateTimeFormatter.ISO_DATE),  // yyyy-MM-dd 형식으로 변환
	                "count", entry.getValue()
	            );
	            return result;
	        })
	        .collect(Collectors.toList());
	}

	public List<Map<String, Object>> getCancelledMemberData(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 날짜 범위에 해당하는 탈퇴한 멤버 조회
        List<Cancel> cancelledMembers = memberRepository.findByCancelDateBetween(startDateTime, endDateTime);

        // 날짜별로 탈퇴한 멤버 수 집계
        return cancelledMembers.stream()
            .collect(Collectors.groupingBy(
                cancel -> cancel.getCancelDate().toLocalDate(), // 날짜별로 그룹핑
                Collectors.counting() // 각 날짜별 개수 계산
            ))
            .entrySet()
            .stream()
            .map(entry -> {
                Map<String, Object> result = Map.of(
                    "date", entry.getKey().format(DateTimeFormatter.ISO_DATE),  // yyyy-MM-dd 형식으로 변환
                    "count", entry.getValue()
                );
                return result;
            })
            .collect(Collectors.toList());
    }

	// 박탈 버튼 클릭시
	@Override
	public void revokeMember(Long memberNo) {
	    // 1. 회원 정보 조회
	    Member member = memberRepository.findById(memberNo)
	        .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
	    
	    // 2. member_status를 'ROLE_CANCEL_PRO'로 변경
	    member.setMemberStatus(MemberStatus.ROLE_CANCEL_PRO);
	    memberRepository.save(member);

	    // 3. pro 테이블에서 해당 member_no에 연관된 pro 조회
	    Pro pro = proRepository.findByMemberMemberNo(memberNo)
	        .orElseThrow(() -> new IllegalArgumentException("프로 회원 정보가 존재하지 않습니다."));

	    // 4. pro_item 테이블에서 해당 pro_no에 연관된 모든 pro_item 삭제
	    List<ProItem> proItems = proItemRepository.findByPro(pro);  // pro와 연관된 pro_items 조회
	    proItemRepository.deleteAll(proItems);  // 연관된 pro_item 삭제

	    // 5. pro 테이블에서 해당 member_no에 연관된 pro 삭제
	    proRepository.delete(pro);  // pro 삭제
	}

	
	// 일반 회원 조회 (memberStatus = 'ROLE_USER' 기준)
	public Page<Member> getUserMembers(Pageable pageable) {
	    return memberRepository.findUserMembers(pageable);  // 'ROLE_USER' 회원만 조회
	}

	// 고수 회원 조회 (memberStatus = 'ROLE_PRO' 기준)
	public Page<ProDTO> getProMembersWithDetails(Pageable pageable) {
	    return memberRepository.findProMembersWithRolePro(pageable);  // 'ROLE_PRO' 고수 회원만 조회
	}

	// 탈퇴 회원 조회 (memberStatus = 'ROLE_CANCEL' 기준)
	public Page<CancelDTO> getCancelMembersWithDetails(Pageable pageable) {
	    return memberRepository.findCancelledMembers(pageable);  // 'ROLE_CANCEL' 탈퇴 회원만 조회
	}

	// 공지/이벤트 게시글 등록
	@Override
	public boolean writeArticle(ArticleImageDTO articleImageDTO) {
		try {
			Article article = new Article();
			article.setSubject(articleImageDTO.getSubject());
			article.setContent(articleImageDTO.getContent());
			article.setView(0);
	        article.setLikes(0);
	        article.setWriteDate(LocalDateTime.now());
	        article.setType(articleImageDTO.getType());
	        article.setService("");
            article.setArea("");
            
            Optional<Member> member = memberRepository.findById(articleImageDTO.getMemberNo());
            if (member.isPresent()) {
                article.setMember(member.get());
            } else {
                return false; // Member가 없으면 실패 처리
            }
            
            Article savedArticle = articleRepository.save(article);

            // 이미지 업로드 및 저장 (이미지가 있을 경우에만 처리)
            if (articleImageDTO.getImageFiles() != null && !articleImageDTO.getImageFiles().isEmpty()) {
                for (MultipartFile imageFile : articleImageDTO.getImageFiles()) {
                    System.out.println("Uploading file: " + imageFile.getOriginalFilename());
                    // 1. 오브젝트 스토리지에 업로드
                    String cloudKey = objectStorageService.uploadFile(bucketName, "storage/", imageFile);

                    System.out.println("Uploaded file cloud key: " + cloudKey);

                    // 2. 업로드된 이미지 정보를 DB에 저장
                    Image image = new Image();
                    image.setArticle(savedArticle); // 게시글과 연결
                    image.setMember(member.get()); // 작성자와 연결
                    image.setImageName(imageFile.getOriginalFilename());
                    image.setImageUuidName(cloudKey); // 스토리지의 키 저장
                    imageRepository.save(image);
                }
            }

            return true; // 성공
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 실패 시
        }

	}
	
	// 공지 게시글 조회
	public List<ArticleImageDTO> getArticles() {
		List<ArticleImageDTO> list = new ArrayList<>();
		List<Article> articlePage = articleRepository.findAllEventArticle();
		for (Article article : articlePage) {
			List<String> uuidList = imageRepository.findByUuid(article.getArticleNo());
			ArticleImageDTO articleDTO = new ArticleImageDTO(article, uuidList);
			list.add(articleDTO);
		}
		return list;
	}
	
	// 공지 상세 게시글 조회
	public ArticleImageDTO getArticle(Long articleNo) {
		Article article = articleRepository.findByArticleNo(articleNo);
		List<String> uuidList = imageRepository.findByUuid(article.getArticleNo());
		ArticleImageDTO articleImageDTO = new ArticleImageDTO(article, uuidList);
		return articleImageDTO;
	}
	
	// 공지 및 이벤트 게시글 수정
	@Override
	@Transactional
	public boolean updateArticle(ArticleImageDTO articleImageDTO) {
	    try {
	        // 기존 게시글 조회
	        Optional<Article> existingArticleOpt = articleRepository.findById(articleImageDTO.getArticleNo());
	        if (!existingArticleOpt.isPresent()) {
	            return false;  // 게시글이 존재하지 않으면 실패
	        }

	        Article existingArticle = existingArticleOpt.get();

	        // 기존 게시글 수정
	        existingArticle.setSubject(articleImageDTO.getSubject());
	        existingArticle.setContent(articleImageDTO.getContent());
	        existingArticle.setType(articleImageDTO.getType());
	        // 수정된 정보는 반영하고, 조회수(view) 및 좋아요(likes)는 수정하지 않음

	        articleRepository.save(existingArticle);  // 변경사항 저장

	        // 삭제할 이미지 처리
	        if (articleImageDTO.getRemoveImageUuidNames() != null && !articleImageDTO.getRemoveImageUuidNames().isEmpty()) {
	            for (String removeImageUuid : articleImageDTO.getRemoveImageUuidNames()) {
	                // DB에서 이미지 삭제
	                int deleted = imageRepository.deleteImageByUuidName(removeImageUuid);
	                if (deleted > 0) {
	                    // NCP 스토리지에서 이미지 삭제
	                    objectStorageService.deleteFile(removeImageUuid, bucketName, "storage/");
	                }
	            }
	        }

	        // 새로운 이미지 업로드
	        if (articleImageDTO.getImageFiles() != null && !articleImageDTO.getImageFiles().isEmpty()) {
	            for (MultipartFile imageFile : articleImageDTO.getImageFiles()) {
	                // 1. NCP Object Storage에 이미지 업로드
	                String cloudKey = objectStorageService.uploadFile(bucketName, "storage/", imageFile);

	                // 2. UUID 중복 체크
	                if (imageRepository.existsByImageUuidName(cloudKey)) {
	                    continue;  // 이미 존재하는 UUID는 저장하지 않음
	                }

	                // 3. 업로드된 이미지 정보 DB에 저장
	                Image newImage = new Image();
	                newImage.setArticle(existingArticle); // 게시글과 연결
	                newImage.setMember(existingArticle.getMember()); // 작성자와 연결
	                newImage.setImageName(imageFile.getOriginalFilename());
	                newImage.setImageUuidName(cloudKey); // 스토리지의 키 저장
	                imageRepository.save(newImage);  // 새 이미지 저장
	            }
	        }

	        return true;  // 수정 성공
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;  // 실패 시
	    }
	}
	
	
	// 공지 및 이벤트 게시글 삭제
	@Override
	@Transactional
	public boolean deleteArticle(Long articleNo, Long memberNo) {
	    try {
	        // 기존 게시글 조회
	        Optional<Article> existingArticleOpt = articleRepository.findById(articleNo);
	        if (!existingArticleOpt.isPresent()) {
	            return false;  // 게시글이 존재하지 않으면 실패
	        }

	        Article existingArticle = existingArticleOpt.get();

	        // 작성자 검증
	        if (!existingArticle.getMember().getMemberNo().equals(memberNo)) {
	            return false;  // 작성자가 아닌 경우 삭제 불가
	        }

	        // 게시글에 연결된 이미지 삭제
	        List<Image> images = imageRepository.findByArticleArticleNo(articleNo);
	        for (Image image : images) {
	            // DB에서 이미지 삭제
	            imageRepository.delete(image);
	            // NCP 스토리지에서 이미지 삭제
	            objectStorageService.deleteFile(image.getImageUuidName(), bucketName, "storage/");
	        }

	        // 게시글 삭제
	        articleRepository.delete(existingArticle);

	        return true;  // 삭제 성공
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;  // 실패 시
	    }
	}




	
}


	

