package pro.article.bean;

import category.bean.MainCategory;
import category.bean.SubCategory;
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

@Entity
@Table(name = "pro_article")
@Data
public class ProArticle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_article_no")
	private Long proArticleNo;

	@OneToOne // 해당 고수의 카테고리 전용 게시판이 존재함
    @JoinColumn(name = "main_cate_no", nullable = false)
    private MainCategory mainCateNo;

	@OneToOne // 해당 고수의 카테고리 전용 게시판이 존재함
	@JoinColumn(name = "sub_cate_no", nullable = false)
	private SubCategory subCateNo;

	@ManyToOne // 고수 게시판에 사용자는 여러개의 게시글을 작성할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private Member memberNo;
}
