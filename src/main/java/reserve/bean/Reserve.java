package reserve.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import member.entity.Member;

@Entity
@Data
public class Reserve {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reserve_no")
	private Long reserveNo;
	
	// 여러 예약이 하나의 회원에 속할 수 있도록 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member memberNo;
}
