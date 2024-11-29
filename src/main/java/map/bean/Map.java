package map.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import member.bean.MemberEntity;

@Entity
@Data
public class Map {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "map_no")
	private Long mapNo;
	
	@OneToOne
    @JoinColumn(name = "member_no", nullable = false)
    private MemberEntity memberNo;
	
	@Column(name = "map_path", length = 3000)
	private String mapPath;
}
