package sistema.Academia_api.mensalidade;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class MensalidadeRequest {
    
    private Long matriculaId;
    
    private BigDecimal valor;

    private LocalDate dataVencimento;


}
