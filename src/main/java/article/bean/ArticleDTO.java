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
    
    
 // 생성자 추가
    public ArticleDTO(Long articleNo, String subject, String content, int view, int type, LocalDateTime writeDate, Long memberNo) {
        this.articleNo = articleNo;
        this.subject = subject;
        this.content = content;
        this.view = view;
        this.type = type;
        this.writeDate = writeDate;
        this.memberNo = memberNo;
    }
    
}
