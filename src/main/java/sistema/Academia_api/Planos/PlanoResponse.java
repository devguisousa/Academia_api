package sistema.Academia_api.Planos;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class PlanoResponse {

    private Long id;

    private String nome;

    private Double valor;

    private Integer duracao;

    private Boolean ativo;

    private  String fotoUrl;
    
}
