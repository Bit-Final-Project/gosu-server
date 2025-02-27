package com.ncp.moeego.article.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.moeego.article.bean.ArticleDTO;
import com.ncp.moeego.article.service.ArticleService;
import com.ncp.moeego.image.bean.ImageDTO;
import com.ncp.moeego.image.service.ImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;
	private final ImageService imageService;

	// 한 페이지에 출력할 게시글 수
	private int pageSize = 10;


	// 전체 게시글 조회 페이징
	@GetMapping("/article")
	public ResponseEntity<Map<String, Object>> getArticles(@RequestParam(value = "pg", required = false, defaultValue = "1") int pg) { 

	    Page<ArticleDTO> articlePage = articleService.getArticleListByPage(pg, pageSize);

	    Map<String, Object> response = new HashMap<>();
	    response.put("content", articlePage.getContent());  // 현재 페이지의 콘텐츠 (게시글 목록)
	    response.put("totalPages", articlePage.getTotalPages());  // 전체 페이지 수
	    response.put("currentPage", articlePage.getNumber() + 1);  // 현재 페이지 번호 (0부터 시작하므로 +1)
	    response.put("totalElements", articlePage.getTotalElements());  // 전체 게시글 수

	    return ResponseEntity.ok(response);
	}
	
	// 타입별 게시글 목록
	@GetMapping("/article/{type}")
	public ResponseEntity<Map<String, Object>> getArticlesByType(
	        @PathVariable("type") String type,
	        @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {

	    int typeCode;
	    switch (type) {
	        case "notices":
	            typeCode = 0;
	            break;
	        case "event":
	            typeCode = 1;
	            break;
	        case "free":
	            typeCode = 2;
	            break;
	        case "qna":
	            typeCode = 3;
	            break;
	        case "pro":
	            typeCode = 4;
	            break;
	        default:
	            return ResponseEntity.badRequest().build(); 
	    }

	    Page<ArticleDTO> articlePage = articleService.getTypeArticles(pg, pageSize, typeCode);

	    Map<String, Object> response = new HashMap<>();
	    response.put("content", articlePage.getContent());  // 현재 페이지의 콘텐츠 (게시글 목록)
	    response.put("totalPages", articlePage.getTotalPages());  // 전체 페이지 수
	    response.put("currentPage", articlePage.getNumber() + 1);  // 현재 페이지 번호 (0부터 시작하므로 +1)
	    response.put("totalElements", articlePage.getTotalElements());  // 전체 게시글 수

	    return ResponseEntity.ok(response);
	}
	

	// 인기글 좋아요 순 목록 페이징
	@GetMapping("/article/hot")
	public ResponseEntity<Map<String, Object>> getHotArticles(@RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {

	    Page<ArticleDTO> articlePage = articleService.getHotArticleByPage(pg, pageSize);

	    Map<String, Object> response = new HashMap<>();
	    response.put("content", articlePage.getContent());  // 현재 페이지의 콘텐츠 (게시글 목록)
	    response.put("totalPages", articlePage.getTotalPages());  // 전체 페이지 수
	    response.put("currentPage", articlePage.getNumber() + 1);  // 현재 페이지 번호 (0부터 시작하므로 +1)
	    response.put("totalElements", articlePage.getTotalElements());  // 전체 게시글 수

	    return ResponseEntity.ok(response);
	}

	// 게시글 상세보기
	@GetMapping("/article/viewpage")
	public ResponseEntity<Map<String, Object>> getArticleView(@RequestParam("article_no") Long articleNo) {
		
		ArticleDTO article = articleService.getArticleViewById(articleNo);
		List<ImageDTO> images = imageService.getImageListByArticleNo(articleNo);
		Map<String, Object> response = new HashMap<>();
		response.put("article", article);
		response.put("images", images);
		
		return ResponseEntity.ok(response);
		
	}
	
	// 마이페이지 작성한 게시글 보기
	@GetMapping("/article/mypage")
	public ResponseEntity<Map<String, Object>> getMyArticles(@RequestParam(name = "member_no") Long member_no,
	        											     @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {
	   
		Page<ArticleDTO> articleList = articleService.getMyArticles(member_no, pg, pageSize);

		Map<String, Object> response = new HashMap<>();
		response.put("content", articleList.getContent());  // 현재 페이지의 콘텐츠 (게시글 목록)
	    response.put("totalPages", articleList.getTotalPages());  // 전체 페이지 수
	    response.put("currentPage", articleList.getNumber() + 1);  // 현재 페이지 번호 (0부터 시작하므로 +1)
	    response.put("totalElements", articleList.getTotalElements());  // 전체 게시글 수

	    return ResponseEntity.ok(response);
	}

	// ncp 추가 게시글 작성
	@PostMapping("/article/write")
	public ResponseEntity<String> writeArticle(
	        @ModelAttribute ArticleDTO articleDTO,  // @RequestBody -> @ModelAttribute로 변경
	        @RequestPart(value = "images", required = false) List<MultipartFile> images) { // images는 선택 사항
	    try {
	        // ArticleDTO에 이미지 파일 리스트 설정 (null 처리)
	        articleDTO.setImageFiles(images == null ? List.of() : images);

	        // 서비스 호출
	        boolean result = articleService.writeArticle(articleDTO);

	        if (result) {
	            return ResponseEntity.ok("게시글이 성공적으로 작성되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("게시글 작성 중 오류가 발생했습니다.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}
	
	// ncp 추가 게시글 수정
	@PutMapping("/article/update/{articleNo}")
	public ResponseEntity<String> updateArticle(
	        @PathVariable("articleNo") Long articleNo,
	        @ModelAttribute ArticleDTO articleDTO,
	        @RequestParam(value = "existingImages", required = false) List<String> existingImageIds, // 유지할 이미지 ID 목록
	        @RequestParam(value = "removedImages", required = false) List<String> removedImageIds,   // 삭제할 이미지 ID 목록
	        @RequestPart(value = "images", required = false) List<MultipartFile> images) {          // 새 이미지
	    try {
	        System.out.println("유지할 이미지: " + existingImageIds);
	        System.out.println("삭제할 이미지: " + removedImageIds);

	        articleDTO.setImageFiles(images == null ? List.of() : images);
	        articleDTO.setExistingImageIds(existingImageIds);
	        articleDTO.setRemovedImageIds(removedImageIds);

	        // 서비스 호출
	        boolean result = articleService.updateArticle(articleNo, articleDTO);

	        if (result) {
	            return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("게시글 수정 중 오류가 발생했습니다.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}

	
	// 게시글 삭제
	@DeleteMapping("/article/delete/{articleNo}")
	public ResponseEntity<String> deleteArticle(@PathVariable("articleNo") Long articleNo) {
	    boolean result = articleService.deleteArticle(articleNo);

	    if (result) {
	        return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
	    } else {
	        return ResponseEntity.badRequest().body("게시글 삭제 중 오류가 발생했습니다.");
	    }
	}
	
	// 조회수 증가
	@PutMapping("/article/hit")
	public ResponseEntity<String> updateArticleViewCount(@RequestParam("articleNo") Long articleNo) {
	    try {
	        // 서비스 호출
	        boolean result = articleService.updateView(articleNo);

	        if (result) {
	            return ResponseEntity.ok("조회수가 성공적으로 증가되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("조회수 증가 중 오류가 발생했습니다.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}
	
	// 좋아요 증가
	@PutMapping("/article/like")
	public ResponseEntity<String> updateArticleLikeCount(@RequestParam("articleNo") Long articleNo) {
	    try {
	        // 서비스 호출
	        boolean result = articleService.updateLike(articleNo);

	        if (result) {
	            return ResponseEntity.ok("좋아요가 성공적으로 증가되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("좋아요 증가 중 오류가 발생했습니다.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}
}