package sistema.Academia_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.mensalidade.Mensalidade;
import sistema.Academia_api.mensalidade.StatusMensalidade;

import java.time.LocalDate;
import java.util.List;

public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {

    List<Mensalidade> findByStatusAndDataVencimentoBefore(
            StatusMensalidade status,
            LocalDate data
    );

    long countByStatus(StatusMensalidade status);
    
}
