package sistema.Academia_api.lancamentos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ResumoFinanceiroResponse {
   
    private BigDecimal totalReceitas;
    
    private BigDecimal totalDespesas;
    
    private BigDecimal saldo;
}
