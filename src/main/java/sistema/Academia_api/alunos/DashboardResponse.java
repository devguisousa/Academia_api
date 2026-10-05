package sistema.Academia_api.alunos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class DashboardResponse {
    
    private long alunosAtivos;

    private long mensalidadesPagas;
    private long mensalidadesPendentes;
    private long mensalidadesAtrasadas;

    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal saldo;

    private long alunosHoje;
    
}
