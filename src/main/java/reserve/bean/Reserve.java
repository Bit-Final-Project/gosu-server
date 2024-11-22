package reserve.bean;

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
@Table(name = "reserve")
@Data
public class Reserve {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reserve_no")
	private Long reserveNo;
	
	// 여러 예약이 하나의 회원에 속할 수 있도록 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "member_no", referencedColumnName = "member_no", nullable = false)
    private Member memberNo;
	// referencedColumnName 이건 참조하는 키가 기본키가 아닐때 사용하는거니까 다 필요없겠네 ? ? ? ? 
}
