package sistema.Academia_api.Matricula;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class MatriculaRequest {

    
    private Long alunoId;

    @NotNull
    private Long planoId;
    
}
