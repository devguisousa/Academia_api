package sistema.Academia_api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.lancamentos.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long> {

    Page<Lancamento> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    
}
