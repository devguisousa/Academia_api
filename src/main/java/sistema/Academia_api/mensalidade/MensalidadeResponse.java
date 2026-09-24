package sistema.Academia_api.mensalidade;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class MensalidadeResponse {

    private Long id;

    private long matriculaId;

    private BigDecimal valor;

    private LocalDate dataVencimento;

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(long matriculaId) {
        this.matriculaId = matriculaId;
    }

    public StatusMensalidade getStatus() {
        return status;
    }

    public void setStatus(StatusMensalidade status) {
        this.status = status;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    private StatusMensalidade status;

    
    
}
