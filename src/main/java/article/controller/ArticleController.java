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
	
	// 전체 게시글
	@GetMapping("/article")
	public ResponseEntity<List<ArticleDTO>> getArticles() {
	    
		List<ArticleDTO> articleList = articleService.getArticleAllList();
		
	    return ResponseEntity.ok(articleList);
	    
	}
	
	// 인기글 조회수별 정렬
	@GetMapping("/article/hot")
	public ResponseEntity<List<ArticleDTO>> getHotArticles() {
	    
		List<ArticleDTO> articleList = articleService.getHotArticle();
		
	    return ResponseEntity.ok(articleList);
	    
	}
	
}
