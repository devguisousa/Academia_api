package sistema.Academia_api.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import sistema.Academia_api.user.User;

public interface UserRepository extends JpaRepository<User,Long> {
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
