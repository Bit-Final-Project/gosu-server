package article.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import article.bean.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{

	
	// 1번 (이벤트 게시글) 만 가져오기
	List<Article> findAllByType(int num);
	
	// 아티클 넘버가 10번인 게시글 가져오기
	@Query("select t from Article t where t.articleNo = :num")
	Article findByArticleNo(@Param("num") int num);

	// 검색한 게시글 조회
	@Query("SELECT a FROM Article a WHERE a.subject LIKE :subject OR a.content LIKE :content")
	List<Article> searchArticles(@Param("subject") String subject, @Param("content") String content);
	
	// 제목만 조회 
	@Query("SELECT a FROM Article a WHERE a.subject LIKE :subject")
	List<Article> searchSubjectArticles(@Param("subject") String subject);
	
	// 제목만 조회 
	@Query("SELECT a FROM Article a WHERE a.subject LIKE :content")
	List<Article> searchContentArticles(@Param("content") String content);










	

	
	
}
