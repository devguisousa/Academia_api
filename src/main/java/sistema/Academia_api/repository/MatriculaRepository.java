package sistema.Academia_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.Matricula.Matricula;
;

public interface MatriculaRepository extends JpaRepository <Matricula, Long> {

Optional<Matricula> findByAluno_IdAndAtivoTrue(Long alunoId);

    boolean existsByAluno_IdAndAtivoTrueAndIdNot(Long alunoId, Long id);
    boolean existsByAluno_IdAndAtivoTrue(Long alunoId);
    boolean existsByNumeroMatricula(String numero);
    

    
}  
