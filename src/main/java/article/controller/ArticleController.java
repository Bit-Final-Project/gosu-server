package article.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import article.bean.ArticleDTO;
import article.service.ArticleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {

	private final ArticleService articleService;

	// 한 페이지에 출력할 게시글 수
	private int pageSize = 10;

	// 전체 게시글 목록
	@GetMapping("/article")
	public ResponseEntity<List<ArticleDTO>> getArticles(@RequestParam(value = "pg", required = false, defaultValue = "1") int pg) { 

		List<ArticleDTO> articleList = articleService.getArticleListByPage(pg, pageSize);

		return ResponseEntity.ok(articleList);

	}
	
	// 타입별 게시글 목록
	@GetMapping("/article/{type}")
	public ResponseEntity<List<ArticleDTO>> getArticlesByType(
	        @PathVariable("type") String type,
	        @RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {
	    
	    int typeCode;
	    switch (type) {
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
	            return ResponseEntity.badRequest().build(); // 잘못된 타입 처리
	    }

	    List<ArticleDTO> articleList = articleService.getTypeArticles(pg, pageSize, typeCode);
	    return ResponseEntity.ok(articleList);
	}
	

	// 인기글 좋아요 순 목록
	@GetMapping("/article/hot")
	public ResponseEntity<List<ArticleDTO>> getHotArticles(@RequestParam(value = "pg", required = false, defaultValue = "1") int pg) {

		List<ArticleDTO> articleList = articleService.getHotArticle(pg, pageSize);

		return ResponseEntity.ok(articleList);

	}

	// 게시글 상세보기
	@GetMapping("/article/viewpage")
	public ResponseEntity<ArticleDTO> getArticleView(@RequestParam("article_no") Long articleNo) {
		
		ArticleDTO article = articleService.getArticleViewById(articleNo);

		return ResponseEntity.ok(article);
	}

}
