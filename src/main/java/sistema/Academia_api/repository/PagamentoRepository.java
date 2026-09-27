package sistema.Academia_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.pagamentos.Pagamento;

public interface  PagamentoRepository extends JpaRepository<Pagamento, Long> {
    
}
