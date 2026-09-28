package sistema.Academia_api.pagamentos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoResponse {

    private Long id;

    private Long mensalidadeId;

    private BigDecimal valor;

    private LocalDate dataPagamento;

    private FormaDePagamento formaDePagamento;
    
}
