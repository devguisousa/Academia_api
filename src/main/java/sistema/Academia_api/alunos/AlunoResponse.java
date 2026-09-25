package sistema.Academia_api.alunos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AlunoResponse {

    private Long id;

    private String nome;

    private String email;

    private LocalDate dataNascimento;

    private String cpf;

    private String telefone;

    private String sexo;

    private String endereco;

    private Boolean ativo;

    private String fotoURL;


}
