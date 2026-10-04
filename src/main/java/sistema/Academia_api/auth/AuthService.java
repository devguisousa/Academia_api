package sistema.Academia_api.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.ServiceSegurity.JwtService;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.Usuario;
import sistema.Academia_api.user.login.LoginRequest;
import sistema.Academia_api.user.login.LoginResponse;
import sistema.Academia_api.user.login.RefreshRequest;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    private final UsuarioRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


     public LoginResponse login (LoginRequest request) {

        Usuario user = userRepository.findByEmail(request.getEmail()).orElseThrow
        (() -> new RuntimeException("Email ou senha inválidos"));

            if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {

                throw new  RuntimeException("Email ou senha inválidos");
                
            }

            String token = jwtService.gerarAcessToken(user);

            LoginResponse response = new LoginResponse();

            response.setAcessToken(token);
            response.setEmail(user.getEmail());

            return response;
            
     }

 public  LoginResponse refresh(RefreshRequest request) {
    String email = jwtService.validarRefreshToken(request.getRefreshToken());

        Usuario user = userRepository.findByEmail(email).orElseThrow
        (() -> new RuntimeException("Email ou senha inválidos"));

        String novoAcess = jwtService.gerarAcessToken(user);

        LoginResponse response = new  LoginResponse();

        response.setAcessToken(novoAcess);
        response.setRefreshToken(request.getRefreshToken());
        response.setEmail(user.getEmail());

        return response;
 }
} 
