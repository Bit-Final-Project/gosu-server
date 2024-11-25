package comment.bean;

import article.bean.Article;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import member.bean.Member;

import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_no")
	private Long commentNo;
	
	@ManyToOne // 여러개의 댓글을 한 게시판에 작성할수있음
    @JoinColumn(name = "article_no", nullable = false)
    private Article articleNo;
	
	@ManyToOne // 여러개의 댓글을 한 사용자가 작성할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member memberNo;
	
	@Column(length = 5000)
	private String content;

	@Column(name="write_date")
	private LocalDateTime writeDate = LocalDateTime.now();

	private int lev;
	
}
