package comment.bean;

import article.bean.Article;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import member.bean.Member;

@Entity
@Table(name="comment")
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
	
	@Column(name = "content", length = 5000)
	private String content;
	
	@Column(name = "lev")
	private int lev;
	
}
