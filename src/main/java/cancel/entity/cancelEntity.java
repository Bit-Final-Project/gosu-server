package cancel.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import member.entity.MemberEntity;

@Entity
@Table(name="cancel")
@Data
public class cancelEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cancel_no")
	private Long cancelNo;
	
	@OneToOne
    @JoinColumn(name = "member_no", nullable = false)
    private MemberEntity memberNo;
	
	@Column(name ="reason", length = 3000)
	private String reason;
	
	@Column(name = "cancel_date")
	private Date cancelDate;
	
	
}
