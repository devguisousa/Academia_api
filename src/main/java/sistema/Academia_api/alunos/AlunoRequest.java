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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    @Email 
    private String email;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPinCatraca() {
        return pinCatraca;
    }

    public void setPinCatraca(String pinCatraca) {
        this.pinCatraca = pinCatraca;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @NotBlank
    private String pinCatraca;
    
    private LocalDate dataNascimento;
    
    @NotBlank 
    private String cpf;
    
    private String telefone;

    public String getNome() {
        return nome;
    }

    private String sexo;
    
    private String endereco;
}
