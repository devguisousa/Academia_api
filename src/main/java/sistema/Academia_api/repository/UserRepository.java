package sistema.Academia_api.repository;

import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    
}
