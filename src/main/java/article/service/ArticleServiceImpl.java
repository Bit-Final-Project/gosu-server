package article.service;

import article.bean.Article;
import article.bean.ArticleDTO;
import article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import member.entity.Member;
import member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
	
	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	
	
	// 현재시간 - 게시글 등록시간 로직 -> ~~~분 전
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
	
	// memberNo맞춰서 이름 가져오는 로직
	private String getMemberNameByMemberNo(Long memberNo) {
        Optional<Member> member = memberRepository.findById(memberNo); // memberNo로 Member 조회
        
        return member.map(Member::getName).orElse("Unknown"); // Member가 없으면 "Unknown" 반환
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
	public Page<ArticleDTO> getHotArticleByPage(int pg, int pageSize) {
	    // Pageable 객체 생성: likes 기준 내림차순 정렬
	    Pageable pageable = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("likes")));
	    
	    // ArticleRepository에서 좋아요 순으로 페이징 처리된 결과 조회
	    Page<Article> articlePage = articleRepository.findAll(pageable);
	    
	    // Article 객체를 ArticleDTO로 변환하여 반환
	    return articlePage.map(article -> {
	        String elapsedTime = ArticleRegistDate(article.getWriteDate());  // 경과 시간 계산
	        String memberName = getMemberNameByMemberNo(article.getMemberNo().getMemberNo());  // 회원 이름 가져오기
	        return new ArticleDTO(
	            article.getArticleNo(),
	            article.getSubject(),
	            article.getContent(),
	            article.getView(),
	            article.getType(),
	            article.getWriteDate(),
	            article.getMemberNo().getMemberNo(),
	            article.getLikes(),
	            elapsedTime,
	            memberName
	        );
	    });
	}

	// 전체 게시글 조회 페이징
	@Override
	public Page<ArticleDTO> getArticleListByPage(int pg, int pageSize) {
	    PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));
	    
	    Page<Article> articlePage = articleRepository.findAll(pageRequest);
	    
	    // Article 객체를 ArticleDTO로 변환하여 Page<ArticleDTO>로 반환
	    return articlePage.map(article -> {
	        String elapsedTime = ArticleRegistDate(article.getWriteDate());
	        String memberName = getMemberNameByMemberNo(article.getMemberNo().getMemberNo());
	        return new ArticleDTO(
	            article.getArticleNo(),
	            article.getSubject(),
	            article.getContent(),
	            article.getView(),
	            article.getType(),
	            article.getWriteDate(),
	            article.getMemberNo().getMemberNo(),
	            article.getLikes(),
	            elapsedTime,
	            memberName
	        );
	    });
	}
	
	
	// Type 별 게시판 조회
	@Override
	public Page<ArticleDTO> getTypeArticles(int pg, int pageSize, int type) {
	    PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

	    Page<Article> articlePage = articleRepository.findByType(type, pageRequest);

	    return articlePage.map(article -> {
	        String elapsedTime = ArticleRegistDate(article.getWriteDate());
	        String memberName = getMemberNameByMemberNo(article.getMemberNo().getMemberNo());
	        return new ArticleDTO(
	            article.getArticleNo(),
	            article.getSubject(),
	            article.getContent(),
	            article.getView(),
	            article.getType(),
	            article.getWriteDate(),
	            article.getMemberNo().getMemberNo(),
	            article.getLikes(),
	            elapsedTime,
	            memberName
	        );
	    });
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
	    String elapsedTime = ArticleRegistDate(article.getWriteDate());
	    String memberName = getMemberNameByMemberNo(article.getMemberNo().getMemberNo());
		return new ArticleDTO(
	            article.getArticleNo(),
	            article.getSubject(),
	            article.getContent(),
	            article.getView(),
	            article.getType(),
	            article.getWriteDate(),
	            article.getMemberNo().getMemberNo(),
	            article.getLikes(),
	            elapsedTime,
	            memberName 
	    );
		
	}

	// 마이페이지 작성한 게시글 조회
	@Override
	public Page<ArticleDTO> getMyArticles(Long member_no, int pg, int pageSize) {
	    PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

	    Page<Article> articlePage = articleRepository.findByMemberNo(member_no, pageRequest);

	    return articlePage.map(article -> {
	        String elapsedTime = ArticleRegistDate(article.getWriteDate());
	        String memberName = getMemberNameByMemberNo(article.getMemberNo().getMemberNo());
	        return new ArticleDTO(
	            article.getArticleNo(),
	            article.getSubject(),
	            article.getContent(),
	            article.getView(),
	            article.getType(),
	            article.getWriteDate(),
	            article.getMemberNo().getMemberNo(),
	            article.getLikes(),
	            elapsedTime,
	            memberName
	        );
	    });
	}


	


	

	
	
	
}
