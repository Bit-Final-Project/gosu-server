package favorite.entity;

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
import pro.entity.ProEntity;

@Entity
@Table(name="favorite")
@Data
public class FavoriteEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favorite_no")
	private Long favorite;

	@ManyToOne // 여러개의 즐겨찾기를 한 사용자가 할수있음
    @JoinColumn(name = "member_no", nullable = false)
    private MemberEntity memberNo;
	
	@ManyToOne // 즐겨찾기에 여러명의 고수가 담길수있음
    @JoinColumn(name = "pro_no", nullable = false)
    private ProEntity proNo;
	
}
