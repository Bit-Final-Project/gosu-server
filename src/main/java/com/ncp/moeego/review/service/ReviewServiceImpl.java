package com.ncp.moeego.review.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.category.entity.MainCategory;
import com.ncp.moeego.category.repository.MainCategoryRepository;
import com.ncp.moeego.common.ConvertDate;
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
	
	// 리뷰 작성
	@Override
	public boolean writeReview(ReviewDTO reviewDTO) {
	    try {
	        // ReviewDTO → Review 변환
	        Review review = new Review();
	        review.setReviewContent(reviewDTO.getReviewContent());
	        review.setStar(reviewDTO.getStar());
	        review.setWriteDate(LocalDateTime.now());

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

	        return true; 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; 
	    }
	}

	// 리뷰 조회
	@Override
	public Page<ReviewDTO> getReviewListByPage(int pg, int pageSize) {

	    PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

	    // 성능 개선을 위한 단일 쿼리로 필요한 데이터 조회
	    Page<Object[]> reviewPage = reviewRepository.findReviewsWithDetails(pageRequest);

	    return reviewPage.map(result -> {
	    	Long reviewNo = (Long) result[0];
	        Review review = (Review) result[1];
	        float star = (float) result[2]; 
	        String proName = (String) result[3]; // 달인 이름
	        String subject = (String) result[4]; // 서비스 이름
	        String memberName = (String) result[5]; // 리뷰 작성자 이름

	        // 작성일 기준 경과 시간 계산
	        String elapsedTime = ConvertDate.calculateDate(review.getWriteDate());

	        return new ReviewDTO(
	        		reviewNo,
	                proName,
	                star,
	                subject,
	                review.getReviewContent(),
	                memberName,
	                review.getWriteDate(),
	                elapsedTime
	        );
	    });
	}

	// 리뷰 삭제
	@Override
	public boolean deleteReview(Long reviewNo) {
		try {
			
			Optional<Review> optionalReview = reviewRepository.findById(reviewNo);
			
			if(optionalReview.isPresent()) {
				Review review = optionalReview.get();
				
				// 리뷰와 연결된 이미지 조회
				List<Image> images = imageRepository.findByReview(review);
				
				if(images != null && !images.isEmpty()) {
					for(Image image : images) {
						// 오브젝트 스토리지 이미지 삭제
						objectStorageService.deleteFile(image.getImageUuidName(), bucketName, "storage/");
						
						// 이미지 엔티티 삭제
						imageRepository.delete(image);
					}
				}
				
				// 리뷰 삭제
				reviewRepository.deleteById(reviewNo);
				return true;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}

}
