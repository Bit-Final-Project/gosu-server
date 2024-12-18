package com.ncp.moeego.review.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.image.bean.ImageDTO;
import com.ncp.moeego.image.service.ImageService;
import com.ncp.moeego.review.bean.ReviewDTO;
import com.ncp.moeego.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

	private final ReviewService reviewService;  
	private final ImageService imageService;
	
	private int pageSize = 5;
	
	// 리뷰 작성
	@PostMapping("/review/write")
	public ResponseEntity<String> writeReview(@ModelAttribute ReviewDTO reviewDTO,
											  @RequestPart(value = "images", required = false) List<MultipartFile> images){
		try {

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
	    response.put("content", reviewPage.getContent());  
	    response.put("totalPages", reviewPage.getTotalPages());  
	    response.put("currentPage", reviewPage.getNumber() + 1);  
	    response.put("totalElements", reviewPage.getTotalElements());  

	    return ResponseEntity.ok(response);
	}
	
	// 리뷰 이미지조회
	@GetMapping("/review/images")
	public ResponseEntity<Map<String, Object>> getReviewImages(){
		
		
		List<ImageDTO> images = imageService.getImageByReviewNo();
		
	    Map<String, Object> response = new HashMap<>();
	    response.put("images", images);

	    return ResponseEntity.ok(response);
	}
	
	// 리뷰 삭제
	@DeleteMapping("/review/delete/{reviewNo}")
	public ResponseEntity<String> deleteReview(@PathVariable("reviewNo") Long reviewNo) {
	    boolean result = reviewService.deleteReview(reviewNo);

	    if (result) {
	        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
	    } else {
	        return ResponseEntity.badRequest().body("리뷰 삭제 중 오류가 발생했습니다.");
	    }
	}
	
	// 내가 작성한 리뷰 조회
	@GetMapping("/review/mypage")
	public ResponseEntity<Map<String, Object>> getMyReviews(@RequestParam(name = "member_no") Long member_no,
		     												@RequestParam(value = "pg", required = false, defaultValue = "1") int pg){
		
		Page<ReviewDTO> reviewPage = reviewService.getMyReviews(member_no,pg, pageSize);
		
	    Map<String, Object> response = new HashMap<>();
	    response.put("content", reviewPage.getContent());  
	    response.put("totalPages", reviewPage.getTotalPages());  
	    response.put("currentPage", reviewPage.getNumber() + 1);  
	    response.put("totalElements", reviewPage.getTotalElements());  

	    return ResponseEntity.ok(response);
	}
	
}
