package sistema.Academia_api.user.login;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class LoginRequest {

    private String nome;

    private String email;
    
    private String senha;
    
}
