package article.bean;

import java.util.Date;

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
@Table(name="article")
@Data
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="article_no")
	private Long articleNo;
	
	@ManyToOne // 여러개의 게시판을 한명의 사용자가 작성할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member memberNo;
	
	@Column(name = "subject", length = 3000)
	private String subject;
	
	@Column(name = "content", length = 5000)
	private String content;
	
	@Column(name="view")
	private int view;
	
	@Column(name="type")
	private int type;
	
	@Column(name="write_date")
	private Date writeDate;
	
}
