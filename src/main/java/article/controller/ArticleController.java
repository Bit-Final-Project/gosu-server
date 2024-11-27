package article.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import article.bean.Article;
import article.bean.ArticleDTO;
import article.service.ArticleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {

	private final ArticleService articleService;
	
	@GetMapping("/article")
	public ResponseEntity<List<ArticleDTO>> getArticles() {
	    List<ArticleDTO> articleList = articleService.getArticleAllList();
	    
	    if (articleList.isEmpty()) {
	        System.out.println("데이터가 없습니다.");
	        return ResponseEntity.noContent().build();
	    }
	    
	    System.out.println("총 게시글 수: " + articleList.size());
	    for (ArticleDTO articleDTO : articleList) {
	        System.out.println(articleDTO);
	    }

	    return ResponseEntity.ok(articleList);
	}
	
}
