package article.bean;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ArticleDTO {
	private Long articleNo;
	private Long memberNo; // Member 엔티티 대신 Member의 ID만 사용
	private String subject;
	private String content;
	private int view;
	private int type;
	private LocalDateTime writeDate;
	private int likes;
	private String elapsedTime; 

	// 생성자 추가
	public ArticleDTO(Long articleNo, String subject, String content, int view, int type, LocalDateTime writeDate,
			Long memberNo, int likes) {
		this.articleNo = articleNo;
		this.subject = subject;
		this.content = content;
		this.view = view;
		this.type = type;
		this.writeDate = writeDate;
		this.memberNo = memberNo;
		this.likes = likes;
	}
	// 날짜 계산 생성자
	public ArticleDTO(Long articleNo, String subject, String content, int view, int type, LocalDateTime writeDate,
			Long memberNo, int likes, String elapsedTime) {
		this.articleNo = articleNo;
		this.subject = subject;
		this.content = content;
		this.view = view;
		this.type = type;
		this.writeDate = writeDate;
		this.memberNo = memberNo;
		this.likes = likes;
		this.elapsedTime = elapsedTime;
	}

}
