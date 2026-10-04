package sistema.Academia_api.user;


import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UsuarioResponse {

    private Long id;
    
    private String nome;

    private String email;

    private Role role;
    
}
