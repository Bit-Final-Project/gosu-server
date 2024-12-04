package member.bean.oauth2;

import lombok.Builder;
import lombok.Data;
import member.entity.MemberStatus;

@Data
@Builder
public class MemberDTO {
    private String email;
    private String name;
    private Integer gender;
    private String phone;
    private String address;
    private MemberStatus memberStatus;
}
