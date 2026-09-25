package sistema.Academia_api.Planos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity
@Table (name = "planos") 
public class Plano {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nome;

    @Column (nullable = false)
    private Double valor;

    @Column (nullable = false)
    private Integer duracao;

    private Boolean ativo = true;

    
}
