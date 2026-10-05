package sistema.Academia_api.frequencia;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import sistema.Academia_api.alunos.Aluno;

@Entity 
@Getter 
@Setter 
@Table(name = "frequencias")
public class Frequencia {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "aluno_id")
    private Aluno aluno;

    @Column(nullable = false)
    private LocalDateTime dataEntrada;

}
