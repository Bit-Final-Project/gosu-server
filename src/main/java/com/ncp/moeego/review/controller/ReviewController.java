package com.ncp.moeego.review.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.review.bean.ReviewDTO;
import com.ncp.moeego.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

	private final ReviewService reviewService;  
	
	private int pageSize = 5;
	
	// 리뷰 작성
	@PostMapping("/review/write")
	public ResponseEntity<String> writeReview(@ModelAttribute ReviewDTO reviewDTO,
											  @RequestPart(value = "images", required = false) List<MultipartFile> images){
		try {
	        // 서비스 호출
			System.out.println(reviewDTO.getReviewContent()  +" : 뭐오세요?");
			System.out.println(reviewDTO.getStar()  +" : 뭐오세요?");
			System.out.println(reviewDTO.getMemberNo()  +" : 뭐오세요?");
			System.out.println(reviewDTO.getProItemNo()  +" : 뭐오세요?");
			System.out.println(reviewDTO.getMainCateNo()  +" : 뭐오세요?");
			
			reviewDTO.setImageFiles(images == null ? List.of() : images);
			
	        boolean result = reviewService.writeReview(reviewDTO);

	        if (result) {
	            return ResponseEntity.ok("리뷰가 작성되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("리뷰 작성 중 오류가 발생했습니다.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}
	
	// 리뷰 조회
	@GetMapping("/review")
	public ResponseEntity<Map<String, Object>> getReviews(@RequestParam(value = "pg", required = false, defaultValue = "1") int pg){
		
		Page<ReviewDTO> reviewPage = reviewService.getReviewListByPage(pg, pageSize);
		
	    Map<String, Object> response = new HashMap<>();
	    response.put("content", reviewPage.getContent());  // 현재 페이지의 콘텐츠 (게시글 목록)
	    response.put("totalPages", reviewPage.getTotalPages());  // 전체 페이지 수
	    response.put("currentPage", reviewPage.getNumber() + 1);  // 현재 페이지 번호 (0부터 시작하므로 +1)
	    response.put("totalElements", reviewPage.getTotalElements());  // 전체 게시글 수

	    return ResponseEntity.ok(response);
	}
}
