package sistema.Academia_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.mensalidade.Mensalidade;

public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {
    
}
