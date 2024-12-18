package com.ncp.moeego.review.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.category.entity.MainCategory;
import com.ncp.moeego.category.repository.MainCategoryRepository;
import com.ncp.moeego.image.entity.Image;
import com.ncp.moeego.image.repository.ImageRepository;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.ncp.service.ObjectStorageService;
import com.ncp.moeego.pro.entity.ProItem;
import com.ncp.moeego.pro.repository.ProItemRepository;
import com.ncp.moeego.review.bean.ReviewDTO;
import com.ncp.moeego.review.entity.Review;
import com.ncp.moeego.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
	
	private final ProItemRepository proItemRepository;
	private final MainCategoryRepository mainCategoryRepository;
	private final MemberRepository memberRepository;
	private final ObjectStorageService objectStorageService;
	private final ImageRepository imageRepository;
	private final ReviewRepository reviewRepository;
	
	
	private String bucketName = "moeego";
	
	
	@Override
	public boolean writeReview(ReviewDTO reviewDTO) {
	    try {
	        // ReviewDTO → Review 변환
	        Review review = new Review();
	        review.setReviewContent(reviewDTO.getReviewContent());
	        review.setStar(reviewDTO.getStar());
	        review.setWriteDate(LocalDateTime.now());

	        // MainCategory 조회 및 설정
	        MainCategory mainCategory = mainCategoryRepository.findById(reviewDTO.getMainCateNo())
	                .orElseThrow(() -> new IllegalArgumentException("Invalid mainCategoryNo"));
	        review.setMainCategory(mainCategory);

	        // ProItem 조회 및 설정
	        ProItem proItem = proItemRepository.findById(reviewDTO.getProItemNo())
	                .orElseThrow(() -> new IllegalArgumentException("Invalid proItemNo"));
	        review.setProItem(proItem);

	        // Member 조회 및 설정
	        Member member = memberRepository.findById(reviewDTO.getMemberNo())
	                .orElseThrow(() -> new IllegalArgumentException("Invalid memberNo"));
	        review.setMember(member);

	        // Review 저장
	        Review savedReview = reviewRepository.save(review);

	        // 이미지 업로드 및 저장 (이미지가 있을 경우에만 처리)
	        if (reviewDTO.getImageFiles() != null && !reviewDTO.getImageFiles().isEmpty()) {
	            for (MultipartFile imageFile : reviewDTO.getImageFiles()) {
	                // 1. 오브젝트 스토리지에 업로드
	                String cloudKey = objectStorageService.uploadFile(bucketName, "storage/", imageFile);

	                // 2. 업로드된 이미지 정보를 DB에 저장
	                Image image = new Image();
	                image.setReview(savedReview); // 리뷰와 연결
	                image.setMember(member); // 작성자와 연결
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

}
