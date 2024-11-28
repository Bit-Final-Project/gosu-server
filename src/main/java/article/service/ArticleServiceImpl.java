package article.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import article.bean.Article;
import article.bean.ArticleDTO;
import article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
	
	private final ArticleRepository articleRepository;
	
	
	
	public String ArticleRegistDate(LocalDateTime writeDate) {
		LocalDateTime now = LocalDateTime.now();

        // 두 시간 간의 차이 계산
        long minutes = ChronoUnit.MINUTES.between(writeDate, now);
        long hours = ChronoUnit.HOURS.between(writeDate, now);
        long days = ChronoUnit.DAYS.between(writeDate, now);

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days < 7) {
            return days + "일 전";
        } else {
            // 7일 이상이면 등록 날짜 출력
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
            return writeDate.format(formatter);
        }
	}
	
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

	// 좋아요 순으로 조회(인기 게시글)
	@Override
	public List<ArticleDTO> getHotArticle(int page, int pageSize) {
		
		
		 PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

		 Page<Article> list = articleRepository.findAllByOrderByLikesDesc(pageRequest);
		
		    return list.getContent().stream()
		            .map(article -> {
		                String elapsedTime = ArticleRegistDate(article.getWriteDate());
		                return new ArticleDTO(
		                        article.getArticleNo(),
		                        article.getSubject(),
		                        article.getContent(),
		                        article.getView(),
		                        article.getType(),
		                        article.getWriteDate(),
		                        article.getMemberNo().getMemberNo(),
		                        article.getLikes(),
		                        elapsedTime // 추가된 필드
		                );
		            })
		            .collect(Collectors.toList());
		
	}

	// 전체 게시글 조회 /article?pg=1,2,3,4 ~~~
	@Override
	public List<ArticleDTO> getArticleListByPage(int page, int pageSize) {
	    // 페이지 번호는 0부터 시작하므로 -1
	    PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));
	    
	    // 페이지 처리된 결과를 가져옴
	    Page<Article> articlePage = articleRepository.findAll(pageRequest);
	    
	    // Article 엔티티를 ArticleDTO로 변환하여 리스트로 반환
	    return articlePage.getContent().stream()
	            .map(article -> {
	                String elapsedTime = ArticleRegistDate(article.getWriteDate());
	                return new ArticleDTO(
	                        article.getArticleNo(),
	                        article.getSubject(),
	                        article.getContent(),
	                        article.getView(),
	                        article.getType(),
	                        article.getWriteDate(),
	                        article.getMemberNo().getMemberNo(),
	                        article.getLikes(),
	                        elapsedTime // 추가된 필드
	                );
	            })
	            .collect(Collectors.toList());
	}

	
	
	// Type 별 게시판 조회
	@Override
	public List<ArticleDTO> getTypeArticles(int pg, int pageSize, int type) {
	    // 페이지 번호는 0부터 시작하므로 -1
	    PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

	    Page<Article> articlePage;

	    articlePage = articleRepository.findByType(type, pageRequest);


	    return articlePage.getContent().stream()
	            .map(article -> {
	                String elapsedTime = ArticleRegistDate(article.getWriteDate());
	                return new ArticleDTO(
	                        article.getArticleNo(),
	                        article.getSubject(),
	                        article.getContent(),
	                        article.getView(),
	                        article.getType(),
	                        article.getWriteDate(),
	                        article.getMemberNo().getMemberNo(),
	                        article.getLikes(),
	                        elapsedTime // 추가된 필드
	                );
	            })
	            .collect(Collectors.toList());
	}
	
	// 게시글 상세 조회
	@Override
	public ArticleDTO getArticleViewById(Long articleNo) {
		Optional<Article> list = articleRepository.findById(articleNo);
		
		// 게시글이 없으면 null 반환
	    if (!list.isPresent()) {
	        return null;
	    }

	    // 게시글이 존재하면 ArticleDTO로 변환하여 반환
	    Article article = list.get();
	    
		return new ArticleDTO(
	            article.getArticleNo(),
	            article.getSubject(),
	            article.getContent(),
	            article.getView(),
	            article.getType(),
	            article.getWriteDate(),
	            article.getMemberNo().getMemberNo(),
	            article.getLikes()
	    );
		
	}


	

	
	
	
}
