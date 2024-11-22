package member.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="member_no")
	private Long memberNo;

	@Column(unique = true)
	private String email;

	private String memberStatus;

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

	private String profileImage;

}