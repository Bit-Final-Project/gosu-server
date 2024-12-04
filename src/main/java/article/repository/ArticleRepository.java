package article.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import article.bean.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{

	// 1번 (이벤트 게시글) 만 가져오기
	@Query("select t from Article t where t.type = :type")
	List<Article> findAllByType(@Param("type") int type);
	
	// 아티클 넘버가 10번인 게시글 가져오기
	@Query("select t from Article t where t.articleNo = :num")
	Article findByArticleNo(@Param("num") int num);

	// 제목 + 내용 검색한 게시글 조회
	@Query("SELECT a FROM Article a WHERE a.subject LIKE CONCAT('%', :subject, '%') AND a.content LIKE CONCAT('%', :content, '%')")
	List<Article> searchArticles(@Param("subject") String subject, @Param("content") String content);
	
	// 제목만 조회 
	@Query("SELECT a FROM Article a WHERE a.subject LIKE CONCAT('%', :subject,'%')")
	List<Article> searchSubjectArticles(@Param("subject") String subject);
	
	// 내용만 조회 
	@Query("SELECT a FROM Article a WHERE a.content LIKE CONCAT('%', :content,'%')")
	List<Article> searchContentArticles(@Param("content") String content);
	
	// 페이징
	@Query("SELECT a FROM Article a ORDER BY a.articleNo DESC")
	Page<Article> findAllWithPaging(Pageable pageable);

	// 인기 게시글 조회
    Page<Article> findAllByOrderByLikesDesc(Pageable pageable);
	
	// 타입별 게시판 조회
	Page<Article> findByType(int type, PageRequest pageRequest);

	// 내가 작성한 게시글 목록
	@Query("SELECT a FROM Article a WHERE a.memberNo.memberNo = :memberNo")
	Page<Article> findByMemberNo(@Param("memberNo") Long memberNo, Pageable pageable);

}
