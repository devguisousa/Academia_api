package sistema.Academia_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.alunos.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Page<Aluno> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    java.util.List<Aluno> findByPinCatraca(String pinCatraca);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    long countByAtivoTrue();
    
}
