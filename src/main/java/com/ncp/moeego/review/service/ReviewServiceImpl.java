package com.ncp.moeego.review.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import com.ncp.moeego.pro.repository.ProRepository;
import com.ncp.moeego.review.bean.ItemReviewResponse;
import com.ncp.moeego.review.bean.ReviewDTO;
import com.ncp.moeego.review.entity.Review;
import com.ncp.moeego.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ProItemRepository proItemRepository;
	private final MainCategoryRepository mainCategoryRepository;
	private final MemberRepository memberRepository;
	private final ObjectStorageService objectStorageService;
	private final ImageRepository imageRepository;
	private final ReviewRepository reviewRepository;
	private final ProRepository proRepository;

	private String bucketName = "moeego";

	// 리뷰 조회
	@Override
	public Page<ReviewDTO> getReviewListByPage(int pg, int pageSize) {

		PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

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

			// 리뷰에 맞는 이미지 UUID 조회
			List<String> imageUuidList = reviewRepository.findImageUuidsByReviewNo(reviewNo);

			Long proItemNo = review.getProItem().getProItemNo();

			return new ReviewDTO(reviewNo, proName, star, subject, review.getReviewContent(), memberName,
					review.getWriteDate(), elapsedTime, proItemNo, imageUuidList);
		});
	}
	
	// 내가 작성한 리뷰 조회
	@Override
	public Page<ReviewDTO> getMyReviews(Long memberNo, int pg, int pageSize) {

		PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

		// memberNo로 리뷰 조회
		Page<Object[]> reviewPage = reviewRepository.findReviewsByMemberNo(memberNo, pageRequest);

		return reviewPage.map(result -> {
			Long reviewNo = (Long) result[0];
			Review review = (Review) result[1];
			float star = (float) result[2];
			String proName = (String) result[3]; // 달인 이름
			String subject = (String) result[4]; // 서비스 이름
			String memberName = (String) result[5]; // 리뷰 작성자 이름

			// 작성일 기준 경과 시간 계산
			String elapsedTime = ConvertDate.calculateDate(review.getWriteDate());

			// 리뷰에 맞는 이미지 UUID 조회
			List<String> imageUuidList = reviewRepository.findImageUuidsByReviewNo(reviewNo);

			Long proItemNo = review.getProItem().getProItemNo();

			return new ReviewDTO(reviewNo, proName, star, subject, review.getReviewContent(), memberName,
					review.getWriteDate(), elapsedTime, proItemNo, imageUuidList);
		});
	}

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

			// 통계 업데이트
			updateProItemStatistics(proItem.getProItemNo());
			updateProStatistics(proItem.getPro().getProNo());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// proItem 통계 업데이트
	@Transactional
	public void updateProItemStatistics(Long proItemNo) {
		// 리뷰 개수 계산
		int reviewCount = reviewRepository.countByProItemNo(proItemNo);
		// star 합계 계산
		float starSum = reviewRepository.sumStarByProItemNo(proItemNo);
		// 평균 star 계산
		float averageStar = reviewCount > 0 ? starSum / reviewCount : 0;
		// ProItem 업데이트
		proItemRepository.updateStatistics(proItemNo, reviewCount, averageStar);
	}

	// pro 통계 업데이트
	@Transactional
	public void updateProStatistics(Long proNo) {
		// ProItem의 reviewCount 합계 계산
		int totalReviewCount = proItemRepository.sumReviewCountByProNo(proNo);
		// ProItem의 star 합계 계산
		float totalStarSum = proItemRepository.sumStarByProNo(proNo);
		// 평균 star 계산
		float averageStar = totalReviewCount > 0 ? totalStarSum / totalReviewCount : 0;
		// Pro 업데이트
		proRepository.updateStatistics(proNo, totalReviewCount, averageStar);
	}

	// 리뷰 삭제
	@Override
	public boolean deleteReview(Long reviewNo) {
		try {
			Optional<Review> optionalReview = reviewRepository.findById(reviewNo);

			if (optionalReview.isPresent()) {
				Review review = optionalReview.get();

				// 리뷰와 연결된 이미지 삭제
				List<Image> images = imageRepository.findByReview(review);
				if (images != null && !images.isEmpty()) {
					for (Image image : images) {
						// 오브젝트 스토리지에서 이미지 삭제
						objectStorageService.deleteFile(image.getImageUuidName(), bucketName, "storage/");

						// 이미지 엔티티 삭제
						imageRepository.delete(image);
					}
				}

				// 리뷰 삭제
				reviewRepository.deleteById(reviewNo);

				// ProItem 통계 업데이트
				Long proItemNo = review.getProItem().getProItemNo();
				updateProItemStatistics(proItemNo); // ProItem의 reviewCount와 star 계산

				// Pro 통계 업데이트
				Long proNo = review.getProItem().getPro().getProNo();
				updateProStatistics(proNo); // Pro의 reviewCount와 star 계산

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Page<ItemReviewResponse> getReviewsByItemNo(Long proItemNo, int pg) {
        Pageable pageable = PageRequest.of(pg - 1, 5);
        Page<ItemReviewResponse> reviewPage = reviewRepository.findReviewsByProItem_ProItemNo(proItemNo, pageable);

        // 리뷰 번호 추출
        List<Long> reviewNos = reviewPage.getContent().stream().map(ItemReviewResponse::getReviewNo).toList();

        // 이미지 데이터를 가져오고 map으로 변환
        List<Object[]> imageData = imageRepository.findImageUuidsByReviewNos(reviewNos);

        Map<Long, List<String>> imageMap = imageData.stream()
                .collect(Collectors.groupingBy(
                        data -> (Long) data[0], // reviewNo
                        Collectors.mapping(
                                data -> (String) data[1], // imageUuidName
                                Collectors.toList()
                        )
                ));

        //리뷰 객체에 이미지 uuid 셋팅
        reviewPage.getContent().forEach(item -> {
            List<String> imageUuids = imageMap.getOrDefault(item.getReviewNo(), List.of());
            item.setImageUuidNames(imageUuids);
        });

        return reviewPage;

    }


}
