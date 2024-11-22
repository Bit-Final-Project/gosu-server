package pro.review.bean;

import java.util.Date;

import category.bean.MainCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import member.bean.Member;
import pro.article.bean.ProAticle;

@Entity
@Table(name = "review")
@Data
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_no")
	private Long reviewNo;

	@ManyToOne // 한 고수게시판에는 여러개의 리뷰가 달릴수있음
	@JoinColumn(name = "pro_article_no", nullable = false)
	private ProAticle proArticleNo;

	@OneToOne // 해당 고수의 카테고리 전용 게시판이 존재함
	@JoinColumn(name = "main_cate_no", nullable = false)
	private MainCategory mainCateNo;

	@ManyToOne // 고수 게시판에 사용자는 여러개의 리뷰를 작성할수있음
	@JoinColumn(name = "member_no", nullable = false)
	private Member memberNo;

	@Column(name = "like")
	private float like;

	@Column(name = "write_date")
	private Date writeDate;

}
