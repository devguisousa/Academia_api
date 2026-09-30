package sistema.Academia_api.lancamentos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
@Table (name = "lancamentos")
public class Lancamento {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String descricao;

    @Column (nullable = false)
    private BigDecimal valor;


    @Column (nullable = false)
    private LocalDate dataMensal;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private TipoLancamento tipo;


}
