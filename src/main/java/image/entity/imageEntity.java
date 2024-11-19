package image.entity;

import article.entity.ArticleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import member.entity.MemberEntity;
import pro.article.entity.ProAticleEntity;
import pro.review.entity.reviewEntity;

@Entity
@Table(name = "image")
@Data
public class imageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_no")
	private Long imageNo;
	
	@ManyToOne // 여러개의 이미지를 한 리뷰에 넣을수있음
	@JoinColumn(name = "review_no", nullable = false)
	private reviewEntity reviewNo;
	
	@ManyToOne // 여러개의 이미지를 고수 게시판에 넣을수있음
	@JoinColumn(name = "pro_article_no", nullable = false)
	private ProAticleEntity proArticleNo;
	
	
	@ManyToOne // 여러개의 이미지를 게시판에 넣을수있음
	@JoinColumn(name = "article_no", nullable = false)
	private ArticleEntity articleNo;
	
	@ManyToOne // 여러개의 이미지를 사용자가 넣을수있음
	@JoinColumn(name = "member_no", nullable = false)
	private MemberEntity memberNo;
	
	@Column(name = "image_name", length = 3000)
	private String imageName;
	
}
