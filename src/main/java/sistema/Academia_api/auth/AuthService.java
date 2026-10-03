package sistema.Academia_api.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.ServiceSegurity.JwtService;
import sistema.Academia_api.repository.UserRepository;
import sistema.Academia_api.user.User;
import sistema.Academia_api.user.login.LoginRequest;
import sistema.Academia_api.user.login.LoginResponse;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


     public LoginResponse login (LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow
        (() -> new RuntimeException("Email ou senha inválidos"));

            if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {

                throw new  RuntimeException("Email ou senha inválidos");
                
            }

            String token = jwtService.gerarToken(user);

            LoginResponse response = new LoginResponse();

            response.setToken(token);
            response.setEmail(user.getEmail());

            return response;
            
     }

} 
