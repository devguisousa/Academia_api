package sistema.Academia_api.Matricula;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class MatriculaResponse {
    
    private Long id;

    private Long alunoId;

    private String nomeAluno;

    private Long planoId;

    private String nomePlano;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Boolean ativo;
   
}