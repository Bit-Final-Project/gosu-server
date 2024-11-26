package admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {

    public LoginRequest() {
    }

    private String email;
    private String pwd;

}
