package sistema.Academia_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.Matricula.Matricula;
;

public interface MatriculaRepository extends JpaRepository <Matricula, Long> {

    boolean existsByAluno_IdAndAtivoTrueAndIdNot(Long alunoId, Long id);
    boolean existsByAluno_IdAndAtivoTrue(Long alunoId);
    boolean existsByNumeroMatricula(String numero);
    

    
}  
