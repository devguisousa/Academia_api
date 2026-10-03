package sistema.Academia_api.ServiceSegurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.repository.UserRepository;
import sistema.Academia_api.user.User;
import sistema.Academia_api.user.login.LoginRequest;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;


     public User login (LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow
        (() -> new RuntimeException("Email ou senha inválidos"));

            if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {

                throw new  RuntimeException("Email ou senha inválidos");
                
            }

            return user;
     }

} 
