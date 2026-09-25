package sistema.Academia_api.Planos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class PlanoRequest {

    @NotBlank 
    private String nome;

    @NotNull
    @Positive 
    private Double valor;
    
    @NotNull 
    @Positive 
    private Integer duracao;
}
