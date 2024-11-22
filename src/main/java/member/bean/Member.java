package member.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import map.bean.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberNo;

	@Column(unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private MemberStatus memberStatus = MemberStatus.USER;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String pwd;

	@Column(nullable = false)
	private Integer gender;

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private String address;

	@Column(name = "profile_image")
	private String profileImage;

	@OneToOne(mappedBy = "memberNo", cascade = CascadeType.ALL)
	private Map map;

}