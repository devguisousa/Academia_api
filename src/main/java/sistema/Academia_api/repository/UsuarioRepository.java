package sistema.Academia_api.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.user.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
