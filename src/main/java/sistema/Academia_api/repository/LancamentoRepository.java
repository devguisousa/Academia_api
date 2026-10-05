package sistema.Academia_api.repository;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.lancamentos.Lancamento;
import sistema.Academia_api.lancamentos.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long> {

    Page<Lancamento> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
    
    Page<Lancamento> findByDataMensalBetween(LocalDate inicio, LocalDate fim, Pageable pageable);
    
    List<Lancamento> findByDataMensalBetween(LocalDate inicio, LocalDate fim);

    BigDecimal countByTipo(TipoLancamento tipo);
    
}
