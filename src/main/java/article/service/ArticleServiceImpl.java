package article.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import article.bean.Article;
import article.bean.ArticlePaging;
import article.dao.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
	
	private final ArticleRepository articleRepository;
	private ArticlePaging articlePaging;
	
	@Override
	public List<Article> getArticleList(int type) {
		return articleRepository.findAllByType(type);
	}

	@Override
	public void write(Article article) {
		articleRepository.save(article);
		
	}

	@Override
	public Article getEventList(int num) {
		
		return articleRepository.findByArticleNo(num);
	}

	@Override
	public void update(Article article) {
		articleRepository.save(article);
		
	}

	@Override
	public void deleteByArticleNo(int num) {
		articleRepository.deleteById((long) num);
	}

	@Override
	public List<Article> searchArticles(String subject, String content) {
		return articleRepository.searchArticles(subject,content);
	}

	@Override
	public List<Article> searchSubjectArticles(String subject) {
		return articleRepository.searchSubjectArticles(subject);
	}

	@Override
	public List<Article> searchContentArticles(String content) {
		return articleRepository.searchContentArticles(content);
	}

	@Override
	public Map<String, Object> getArticleList(Pageable pageable) {
	    Page<Article> list = articleRepository.findAll(pageable);
	    
	    // 전체 게시글 수를 효율적으로 가져오기
	    long totalA = articleRepository.count();
	    System.out.println("Total Articles: " + totalA);
	    
	    // articlePaging 초기화 확인
	    if (articlePaging == null) {
	        articlePaging = new ArticlePaging();
	    }
	    
	    articlePaging.setCurrentPage(pageable.getPageNumber() + 1);
	    articlePaging.setPageBlock(5);
	    articlePaging.setPageSize(10);
	    articlePaging.setTotalA((int) totalA);  // long을 int로 변환
	    articlePaging.makePagingHTML();
	    
	    // 디버깅을 위한 페이징 HTML 출력
	    System.out.println("Paging HTML: " + articlePaging.getPagingHTML());
	    
	    Map<String, Object> map = new HashMap<>();
	    map.put("list", list);
	    map.put("articlePaging", articlePaging);
	    
	    return map;
	}


	

	

	
	
	
}
