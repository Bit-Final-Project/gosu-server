package article.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import article.bean.Article;
import article.bean.ArticleDTO;

public interface ArticleService {

	public List<Article> getArticleList(int type);

	
	public void write(Article article);

	public Article getEventList(int num);

	
	public void update(Article article);
	public void deleteByArticleNo(int num);

	
	public List<Article> searchArticles(String subject, String Content);
	public List<Article> searchSubjectArticles(String keyword);
	public List<Article> searchContentArticles(String keyword);
		
	public List<ArticleDTO> getHotArticle(int pg, int pageSize);
	public List<ArticleDTO> getArticleListByPage(int page, int pageSize);


	// 게시글 상세 조회
	public ArticleDTO getArticleViewById(Long articleNo);

	// Type 별 게시판 조회
	public List<ArticleDTO> getTypeArticles(int pg, int pageSize, int type);

}
