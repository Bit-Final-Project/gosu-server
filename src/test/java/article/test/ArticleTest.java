package article.test;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.main.moeego.MoeegoApplication;

import article.bean.Article;
import article.service.ArticleService;

@SpringBootTest(classes = MoeegoApplication.class)
class ArticleTest {
	
	@Autowired
	private ArticleService articleService;

	
	// 전체 게시판 가져오기 + 페이징
	@Test
	@Disabled
	void contextLoads() {

	}
	
	
	// Type 별로 게시판 조회
	@Test
	void articleTypeSelect() {

	}
	
	
    // 특정(이벤트) 게시판 조회
	/*
	 * @Test
	 * 
	 * @Disabled void eventList() { Article article = new Article();
	 * article.setType(1);
	 * 
	 * int num = article.getType();
	 * 
	 * List<Article> list = articleService.getEventList(num);
	 * 
	 * list.forEach(System.out::println); }
	 */
    
	/*
	 * // MemberNo 별로 게시글 작성
	 * 
	 * @Test
	 * 
	 * @Disabled void articleWrite() { Article article = new Article(); Member
	 * member = new Member(); member.setMemberNo(1L);
	 * 
	 * article.setSubject("test"); article.setContent("test 내용입니다");
	 * article.setView(20); article.setType(2); article.setMemberNo(member);
	 * article.setWriteDate(LocalDateTime.now());
	 * 
	 * articleService.write(article);
	 * 
	 * }
	 */
    
    // 특정 게시글 수정(글 번호로 식별)
    @Test
    @Disabled
    void articleUpdate() {
    	int num = 14;
    	Article article = articleService.getEventList(num);
    	if (article != null) {  // null 체크 필수
    	    article.setSubject("윤강준");
    	    article.setContent("테스트한다");
    	    article.setView(20);
    	    article.setType(2);
    	    article.setWriteDate(LocalDateTime.now());

    	    articleService.update(article); // 업데이트 호출
    	} else {
    	    System.out.println("No article found for the given criteria.");
    	}
    	
    	
    }
    
    // 특정 게시글 삭제(글 번호로 식별)
    @Test
    @Disabled
    void articleDelte() {
    	int num = 10; // 삭제할 게시글 번호
        articleService.deleteByArticleNo(num);
        System.out.println("Article deleted: " + num);
    }
    
    
    // 제목 + 내용 검색한 게시글 출력
    @Test
    @Disabled
    void searchArticles() {
        String keyword = "게시"; // 검색할 키워드

        List<Article> articles = articleService.searchArticles(keyword, keyword);
        articles.forEach(System.out::println);
        
    }
    
    // 제목으로 검색한 게시글 출력
    @Test
    @Disabled
    void searchSubjectArticles() {
        String keyword = "다섯"; // 검색할 키워드

        // 검색된 글 목록 가져오기
        List<Article> articles = articleService.searchSubjectArticles(keyword);

        // 검색된 글 출력
        articles.forEach(System.out::println);
        
    }
    
    // 내용으로 검색한 게시글 출력
    @Test
    //@Disabled
    void searchContentArticles() {
        String keyword = "테스트"; // 검색할 키워드

        // 검색된 글 목록 가져오기
        List<Article> articles = articleService.searchContentArticles(keyword);

        // 검색된 글 출력
        articles.forEach(System.out::println);
        
    }
    

    
    
    
    
    
    
    
    
    
    
    

}
