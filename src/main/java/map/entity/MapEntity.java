package map.entity;

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
@Table(name="map")
@Data
public class MapEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "map_no")
	private Long mapNo;
	
	@OneToOne
    @JoinColumn(name = "member_no", nullable = false)
    private MemberEntity memberNo;
	
	@Column(name="map_path", length = 3000)
	private String mapPath;
	
}
