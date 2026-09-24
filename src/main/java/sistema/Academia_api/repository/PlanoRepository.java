package sistema.Academia_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sistema.Academia_api.Planos.Plano;

public interface PlanoRepository extends JpaRepository<Plano, Long> {
    
    
}
