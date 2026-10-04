package sistema.Academia_api.alunos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class PerfilAlunoResponse {
     private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private Boolean ativo;
    private String fotoURL;

    private String plano;
    private LocalDate inicioMatricula;
    private LocalDate fimMatricula;
    private Boolean matriculaAtiva;

    private Boolean compareceuHoje;
    private LocalDateTime ultimaEntrada;
    private long diasSemComparecer;
}
