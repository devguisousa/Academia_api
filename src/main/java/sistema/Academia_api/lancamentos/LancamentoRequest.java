package sistema.Academia_api.lancamentos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class LancamentoRequest {
    
    @NotBlank 
    private String descricao;
    
    @NotNull 
    @Positive 
    private BigDecimal valor;

    @NotNull 
    private LocalDate dataMensal;

    @NotNull 
    private TipoLancamento tipo;
}
