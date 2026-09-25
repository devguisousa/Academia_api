package sistema.Academia_api.mensalidade;



import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import sistema.Academia_api.Matricula.Matricula;

@Entity 
@Getter 
@Setter 
@Table (name = "mensalidades")
public class Mensalidade {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "matricula_id", nullable = false )
    private Matricula matricula;

    @Column (nullable = false)
    private BigDecimal valor;

    @Column (nullable = false)
    private LocalDate dataVencimento;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private StatusMensalidade status = StatusMensalidade.PENDENTE;

}
