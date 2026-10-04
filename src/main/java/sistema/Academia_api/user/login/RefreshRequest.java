package sistema.Academia_api.user.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class RefreshRequest {

    @NotBlank 
    private String refreshToken;
    
}
