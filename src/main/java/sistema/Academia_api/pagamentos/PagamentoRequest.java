package sistema.Academia_api.pagamentos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class PagamentoRequest {

    @NotNull 
    private Long mensalidadeId;

    @NotNull 
    @Positive 
    private BigDecimal valor;
    
    @NotNull 
    private FormaDePagamento formaDePagamento;
}
