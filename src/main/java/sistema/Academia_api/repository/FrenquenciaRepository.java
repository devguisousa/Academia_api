package sistema.Academia_api.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sistema.Academia_api.frequencia.Frequencia;

public interface FrenquenciaRepository extends JpaRepository<Frequencia, Long> {
    Page<Frequencia> findByAluno_NomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

    boolean existsByAluno_IdAndDataEntradaBetween(
        Long alunoId,
        LocalDateTime inicio,
        LocalDateTime fim
);

Optional<Frequencia> findTopByAluno_IdOrderByDataEntradaDesc(Long alunoId);

Page<Frequencia> findByAluno_Id(
        Long alunoId,
        Pageable pageable
);

@Query("""
    SELECT COUNT(DISTINCT f.aluno.id)
    FROM Frequencia f
    WHERE f.dataEntrada BETWEEN :inicio AND :fim
""")
long contarAlunosPorPeriodo(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
);
    
}
