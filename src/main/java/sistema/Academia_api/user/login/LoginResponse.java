package sistema.Academia_api.user.login;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class LoginResponse {

    private String refreshToken;
    private String acessToken;
    private String email;
    
    
}
