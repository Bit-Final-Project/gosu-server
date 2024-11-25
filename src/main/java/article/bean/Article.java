package article.bean;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import member.bean.Member;

@Entity
@Data
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_no")
	private Long articleNo;
	
	@ManyToOne(fetch = FetchType.LAZY) // 여러개의 게시판을 한명의 사용자가 작성할수있음
    @JoinColumn(name="member_no", nullable = false)
    private Member memberNo;
	
	@Column(length = 3000)
	private String subject;
	
	@Column(length = 5000)
	private String content;

	private int view;

	private int type;

	@Column(name = "write_date")
	private LocalDateTime writeDate = LocalDateTime.now();
	
	
	@Override
	public String toString() {
	    return "Article{" +
	           "articleNo=" + articleNo +
	           ", subject='" + subject + '\'' +
	           ", content='" + content + '\'' +
	           ", view=" + view +
	           ", type=" + type +
	           ", writeDate=" + writeDate +
	           '}';
	}
	
}
