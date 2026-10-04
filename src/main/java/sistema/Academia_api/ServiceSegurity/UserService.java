package sistema.Academia_api.ServiceSegurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.Usuario;
import sistema.Academia_api.user.UsuarioRequest;
import sistema.Academia_api.user.UsuarioResponse;

@Service 
@RequiredArgsConstructor 
public class UserService {
    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private Usuario converterUser (UsuarioRequest request) {
        Usuario user = new Usuario();

        user.setEmail(request.getEmail());
        user.setNome(request.getNome());
        user.setSenha(passwordEncoder.encode(request.getSenha()));
        user.setRole(request.getRole());

        return user;
    }

    private UsuarioResponse converterUserResponse(Usuario user) {
        UsuarioResponse userResponse = new UsuarioResponse();

        userResponse.setId(user.getId());
        userResponse.setNome(user.getNome());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());

        return userResponse;
    }

    private Usuario salvaUser (Usuario user) {
        return userRepository.save(user);
    }

    public  UsuarioResponse cadastrar (UsuarioRequest request) {
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email ja cadastrado");
            
        }
        
        Usuario user = converterUser(request);
        Usuario userSalvo = salvaUser(user);
        return converterUserResponse(userSalvo);
    }
    
}
