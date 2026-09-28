package sistema.Academia_api.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.frenquencia.Frequencia;

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
    
}
