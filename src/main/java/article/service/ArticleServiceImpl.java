package article.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import article.bean.Article;
import article.dao.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
	
	private final ArticleRepository articleRepository;

	@Override
	public List<Article> getArticleList() {
		return articleRepository.findAll();
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
		return articleRepository.searchArticles("%" + subject + "%", "%" + content + "%");
	}

	@Override
	public List<Article> searchSubjectArticles(String subject) {
		return articleRepository.searchSubjectArticles("%" + subject + "%");
	}

	@Override
	public List<Article> searchContentArticles(String content) {
		return articleRepository.searchSubjectArticles("%" + content + "%");
	}



	

	

	
	
	
}
