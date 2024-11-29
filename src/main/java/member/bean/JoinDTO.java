package member.bean;

import lombok.Data;

@Data
public class JoinDTO {
    private String email;
    private String name;
    private String pwd;
    private Integer gender;
    private String phone;
    private String address;
}
