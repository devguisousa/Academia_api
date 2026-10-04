package sistema.Academia_api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UsuarioRequest {

    @NotBlank 
    private String nome;

    @NotBlank 
    @Email 
    private String email;

    @NotBlank 
    @Size(min = 5)
    private String senha;

    @NotNull 
    private Role role;
    
}
