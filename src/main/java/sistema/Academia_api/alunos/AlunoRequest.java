package sistema.Academia_api.alunos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlunoRequest {

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String pinCatraca;

    private LocalDate dataNascimento;

    @NotBlank
    private String cpf;

    private String telefone;

    private String sexo;

    private String endereco;

}
