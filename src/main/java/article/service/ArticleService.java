package article.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import article.bean.Article;

public interface ArticleService {

	public List<Article> getArticleList();
	public void write(Article article);
	public Article getEventList(int num);
	public void update(Article article);
	public void deleteByArticleNo(int num);
	public List<Article> searchArticles(String subject, String Content);
	public List<Article> searchSubjectArticles(String keyword);
	public List<Article> searchContentArticles(String keyword);
	

}
