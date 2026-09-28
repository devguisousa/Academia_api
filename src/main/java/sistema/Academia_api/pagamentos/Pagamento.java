package sistema.Academia_api.pagamentos;


import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import sistema.Academia_api.mensalidade.Mensalidade;

@Entity 
@Getter 
@Setter 
@Table (name = "pagamentos")
public class Pagamento {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne 
    @JoinColumn(name="mensalidade_id")
    private Mensalidade mensalidade;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column (nullable = false)
    private LocalDate dataPagamento;

    private FormaDePagamento formaDePagamento;




}
