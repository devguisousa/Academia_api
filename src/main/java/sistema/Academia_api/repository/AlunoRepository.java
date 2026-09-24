package sistema.Academia_api.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.alunos.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findById(Long id);

    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
    
}
