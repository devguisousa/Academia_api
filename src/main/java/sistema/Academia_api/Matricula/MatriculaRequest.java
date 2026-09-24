package sistema.Academia_api.Matricula;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class MatriculaRequest {

    @NotNull 
    private Long alunoId;

    public Long getPlanoId() {
        return PlanoId;
    }

    public void setPlanoId(Long planoId) {
        PlanoId = planoId;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    @NotNull
    private Long PlanoId;
    
}
