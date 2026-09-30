package sistema.Academia_api.lancamentos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class LancamentoResponse {

    private Long id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate dataMensal;
    
    private TipoLancamento tipo;


    
}
