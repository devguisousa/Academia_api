package sistema.Academia_api.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.user.Usuario;
import sistema.Academia_api.user.Role;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    
    Optional<Usuario> findByEmail(String email);

    boolean existsByRole(Role role);

    boolean existsByEmail(String email);
}
