package sistema.Academia_api.frequencia;



import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class FrequenciaRequest {

    @NotNull
    private Long alunoId;
    
}
