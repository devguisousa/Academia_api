package sistema.Academia_api.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sistema.Academia_api.ServiceSegurity.JwtService;
import sistema.Academia_api.exception.CredenciaisInvalidasException;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.Usuario;
import sistema.Academia_api.user.login.LoginRequest;
import sistema.Academia_api.user.login.LoginResponse;
import sistema.Academia_api.user.login.RefreshRequest;

@Slf4j 
@Service 
@RequiredArgsConstructor 
public class AuthService {
    private final UsuarioRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


     public LoginResponse login (LoginRequest request) {

        Usuario user = userRepository.findByEmail(request.getEmail()).orElseThrow
        (() -> new CredenciaisInvalidasException("Email ou senha inválidos"));

            if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {

                log.warn("Tentativa de login inválida usuarioId={}", user.getId());

                throw new  CredenciaisInvalidasException ("Email ou senha inválidos");
                
            }

            String accesstoken = jwtService.gerarAcessToken(user);

            String refreshToken = jwtService.gerarRefreshToken(user);

            LoginResponse response = new LoginResponse();

            response.setAcessToken(accesstoken);
            response.setRefreshToken(refreshToken);
            response.setEmail(user.getEmail());

            log.info("Login realizado com sucesso usuarioId={}", user.getId());

            return response;
            
     }

 public  LoginResponse refresh(RefreshRequest request) {
    String email = jwtService.validarRefreshToken(request.getRefreshToken());

        Usuario user = userRepository.findByEmail(email).orElseThrow
        (() -> new CredenciaisInvalidasException("Email ou senha inválidos"));

        String novoAcess = jwtService.gerarAcessToken(user);

        LoginResponse response = new  LoginResponse();

        response.setAcessToken(novoAcess);
        response.setRefreshToken(request.getRefreshToken());
        response.setEmail(user.getEmail());

        log.info("Access token renovado usuarioId={}", user.getId());

        return response;
 }
} 
