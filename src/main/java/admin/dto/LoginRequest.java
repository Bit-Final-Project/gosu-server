package admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    public LoginRequest() {
    }

    private String email;
    private String pwd;

}
