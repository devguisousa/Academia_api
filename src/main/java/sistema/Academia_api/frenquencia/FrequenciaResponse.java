package sistema.Academia_api.frenquencia;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class FrequenciaResponse {

    private Long id;

    private Long alunoId;

    private LocalDateTime dataEntrada;

    
}
